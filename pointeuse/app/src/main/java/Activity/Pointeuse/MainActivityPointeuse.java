package Activity.Pointeuse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapters.CustomAdapter;
import Model.Absence;
import Model.Absence_details;
import Model.Classe;
import Model.Etudiant;
import Model.ListPresenceMail;
import Model.Prof;
import database.DBManager;
import database.DatabaseHelper;
import wolfsoft1.pay2wallet.R;


public class MainActivityPointeuse extends AppCompatActivity {

    Absence absence = new Absence();
    ArrayList<Absence_details> absence_details = new ArrayList<Absence_details>();
    ListView listPresence;
    CustomAdapter customAdapter;
    LinearLayout envoyer;
    LinearLayout ajouter;
    DBManager manager;
    List<Etudiant> Etudiants =  new ArrayList<>();
    Classe ClasseApointer;
    DatabaseHelper dh;
    ListPresenceMail listPresenceMail = new ListPresenceMail();
    List<Etudiant> ListAllEtudiants = new ArrayList<>();
    List<Etudiant>  ListEtudiantsNoPresent = new ArrayList<>();

    private PdfPCell cell;
    private Image bgImage;
    private String path;
    private File dir;
    private File file;
    Uri URI = null;
    BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
    BaseColor myColor1 = WebColors.getRGBColor("#757575");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scan);
        initView();
        Intent i = getIntent();
        manager = new DBManager(this);
        dh = new DatabaseHelper(this);

        // la classe à pointer ex : GI / LP / année1
        ClasseApointer = (Classe) i.getSerializableExtra("classe");


        /// Creation d'objet Absence
        Date currentTime = Calendar.getInstance().getTime();
        absence = new Absence(-1, ""+currentTime, "1", ClasseApointer.getIdClasse());


        //start Scan
        Scan();


        for (int j = 0 ; j<getEtudiantsbyIDclasse(ClasseApointer.getIdClasse()).size();j++){
            Log.d("cho",""+getEtudiantsbyIDclasse(ClasseApointer.getIdClasse()).get(j).getCNE());
        }

        try {
            List<Classe> classes = dh.getAll(Classe.class);
        }

        catch (Exception e){

        }
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Ansence PDF";
        dir = new File(path);
        if (!dir.exists()) {
            Toast.makeText(getApplicationContext() , "Directory will be created ", Toast.LENGTH_SHORT).show();
            dir.mkdirs();
        }else{
            Toast.makeText(getApplicationContext() , "Already exist ", Toast.LENGTH_SHORT).show();
        }

        // à la fin de pointage : envoi de la list par email  / mail
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivityPointeuse.this);
                alertDialogBuilder.setMessage("Envoyer la liste d'absence pour cette séance");
                alertDialogBuilder.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        ///// => Construction de la liste à envoyer par mail
                        // envoyer la liste absence_details + absence
                        listPresenceMail.setAbsence(absence);
                        listPresenceMail.setAbsence_details(absence_details);
                        // Creation de la liste des etudiants present :
                        String date = listPresenceMail.getAbsence().getDate_time();

                        listPresenceMail.setEtudiantspresent(getEtudiantsPresent(absence_details));

                        //Classe Currentclasse = manager.getClasseByID(id_Classe).get(0);

                        int  id_Classe = listPresenceMail.getAbsence().getIDClasse();
                        //getEtudiantsAbsent(id_Classe) ;

                        //All students...
                        ListAllEtudiants = manager.getEtudiantsByClasse(id_Classe);
                        for(int i = 0 ; i<ListAllEtudiants.size();i++){
                               ListEtudiantsNoPresent.add(ListAllEtudiants.get(i));
                               Log.d("AllStudents", ""+ListEtudiantsNoPresent.get(i).getNom());
                        }
                        //Only present students
                        for(int j = 0 ; j<listPresenceMail.getEtudiantspresent().size();j++) {
                               ListEtudiantsNoPresent.add(ListAllEtudiants.get(j));
                               Log.d("PresentStudents", ""+listPresenceMail.getEtudiantspresent().get(j).getNom());
                        }
                        //Only absent students
                        ListAllEtudiants = manager.getEtudiantsByClasse(id_Classe);

                         for(int i = 0 ; i<ListAllEtudiants.size();i++){
                            for(int j = 0 ; j<listPresenceMail.getEtudiantspresent().size();j++) {

                                if(!ListAllEtudiants.get(i).getPrenom().equals(listPresenceMail.getEtudiantspresent().get(j).getPrenom())){
                                    ListEtudiantsNoPresent.add(ListAllEtudiants.get(i));
                                      Log.d("AbsentStudents", ""+ListEtudiantsNoPresent.get(i).getNom());
                                 }

                            }
                        }

                        try {
                            createPDF();
                            SendMailPDf();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                       // SendMailPDf(listPresenceMail.getEtudiantspresent(),Currentclasse, date);
                    }
                });
                alertDialogBuilder.setNegativeButton("annuler", null);
                alertDialogBuilder.show();
            }
        });

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scan();
            }
        });


    }


    void initView() {
        listPresence = (ListView) findViewById(R.id.listPresence);
        envoyer = (LinearLayout) findViewById(R.id.envoyer);
        ajouter = (LinearLayout) findViewById(R.id.ajouter);

    }

    void Scan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);
        integrator.initiateScan();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            Etudiant etudiantsscanner = new Etudiant();
            Log.d("scanned",""+scanContent);
            if(getEtudiantsbyIDclasseandCne(scanContent,ClasseApointer.getIdClasse()).size()==0){
                Toast.makeText(this, "Ce etudiant n'appartient pas a ce classe", Toast.LENGTH_SHORT).show();
            }
            else {
                etudiantsscanner = getEtudiantsbyIDclasseandCne(scanContent,ClasseApointer.getIdClasse()).get(0);

                //  etudiantsscanner = getEtudiantsbyIDclasseandCne("11111",ClasseApointer.getIdClasse()).get(0);

                absence_details.add(new Absence_details(absence.getIdAbsence(), scanContent, false));
                customAdapter = new CustomAdapter(this, absence_details);
                listPresence.setAdapter(customAdapter);
            }



        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void updatesList() {
        customAdapter = new CustomAdapter(this, absence_details);
        listPresence.setAdapter(customAdapter);
    }

   public  List<Etudiant> getEtudiantsbyIDclasse(int idClasse){
       return  manager.getEtudiantsByidClass(idClasse);

    }

    public  List<Etudiant> getEtudiantsbyIDclasseandCne(String CNE,int idClasse){
        return  manager.getEtudiantByCNEAndIDclasse(CNE,idClasse);

    }
    public  List<Etudiant> getEtudiantsPresent(ArrayList<Absence_details> absence_details){

        List<Etudiant> etudiants = new ArrayList<>();
        for(int i = 0 ; i<absence_details.size();i++){
            etudiants.add(manager.getEtudiantByCNE(absence_details.get(i).getCNE()).get(0));
        }
        return  etudiants;

    }
    public List<Prof> getProfById(String idProf){
        return  manager.getProfByID(idProf);

    }


    void SendMailPDf(){

        if( isNetworkAvailable(this) ){

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            try
            {

                String email = "laarajoussama25@gmail.com";
                String subject = "List d'Absences !!";
                String message = "List d'absences pour la seance "+sdf.format(Calendar.getInstance().getTime());
                Uri URI = Uri.parse("file://" +file.getAbsolutePath());

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { email });
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
                if (URI != null) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
                }
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                this.startActivity(Intent.createChooser(emailIntent,"Sending email..."));
            }
            catch (Throwable t)
            {
                Toast.makeText(this, "Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Ur Not Connected to Net", Toast.LENGTH_SHORT).show();
        }

        //  classe.getAnnee();
       // classe.getCycle();
       // classe.getFillier();
    }
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

   public List<Etudiant> getEtudiantsAbsent(int idClasse){

        ListAllEtudiants = manager.getEtudiantsByClasse(idClasse);
          for(int i = 0 ; i<ListAllEtudiants.size();i++){
            for(int j = 0 ; j<listPresenceMail.getEtudiantspresent().size();j++) {
               if(ListAllEtudiants.get(i).getCNE() != listPresenceMail.getEtudiantspresent().get(j).getCNE()){
                   ListEtudiantsNoPresent.add(ListAllEtudiants.get(i));
                   // Log.d("ListAllEtudiants", ""+ListAllEtudiants.get(i);
                   Log.d("ListEtudiantsNoPresent", ""+ListEtudiantsNoPresent.get(i).getNom());
               }
              //  Log.d("listPresenceMail", ""+listPresenceMail.getEtudiantspresent().get(j).getNom());
            }
        }
        return ListEtudiantsNoPresent;
    }

    public void createPDF() throws FileNotFoundException, DocumentException {

        //create document file
        Document doc = new Document();
        try {

            Log.e("PDFCreator", "PDF Path: " + path);
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            file = new File(dir, "L3 Info" + sdf.format(Calendar.getInstance().getTime()) + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            //create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.ALIGN_BASELINE);

            //set drawable in cell
            Drawable myImage = MainActivityPointeuse.this.getResources().getDrawable(R.drawable.upec);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            try {
                bgImage = Image.getInstance(bitmapdata);
                bgImage.setAbsolutePosition(330f, 642f);
                cell.addElement(bgImage);
                pt.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Paragraph("Absence List"));
                cell.addElement(new Paragraph(""));
                cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);
                //Header
                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                //Corp
                PdfPTable table = new PdfPTable(3);

                float[] columnWidth = new float[]{30, 30, 30};
                table.setWidths(columnWidth);

                cell = new PdfPCell();
                cell.setBackgroundColor(myColor);
                cell.setColspan(3);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setColspan(3);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(3);
                cell.setBackgroundColor(myColor1);


                cell = new PdfPCell(new Phrase("idNumber"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("lastName"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("firstName"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(3);

                ListAllEtudiants = manager.getEtudiantsByClasse(ClasseApointer.getIdClasse()) ;

                List<Etudiant> listetudiantpresent = listPresenceMail.getEtudiantspresent() ;

                for(int i = 0 ; i<listetudiantpresent.size();i++){
                    table.addCell(listetudiantpresent.get(i).getCNE());
                    table.addCell(listetudiantpresent.get(i).getNom());
                    table.addCell(listetudiantpresent.get(i).getPrenom());
                }

                PdfPTable ftable = new PdfPTable(3);
                ftable.setWidthPercentage(100);
                float[] columnWidthaa = new float[]{10,10,10};
                ftable.setWidths(columnWidthaa);
                cell = new PdfPCell();
                cell.setColspan(3);
                cell.setBackgroundColor(myColor1);

                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""       ));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

                int  id_Classe = listPresenceMail.getAbsence().getIDClasse();
                Classe Currentclasse = manager.getClasseByID(id_Classe).get(0);

                cell = new PdfPCell(new Paragraph("Date da la seance : "+sdf1.format(Calendar.getInstance().getTime())+"                   "
                            +manager.getClasseByID(id_Classe).get(0).getCycle()+" "
                            +manager.getClasseByID(id_Classe).get(0).getFillier() ));
                cell.setColspan(3);
                ftable.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(3);
                cell.addElement(ftable);
                table.addCell(cell);
                doc.add(table);
                Toast.makeText(getApplicationContext(), "PDF created", Toast.LENGTH_LONG).show();
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                Log.e("PDFCreator", "ioException:" + e);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}