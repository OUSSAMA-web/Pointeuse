package Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import Model.Absence;
import Model.Absence_details;
import Model.Etudiant;
import database.DBManager;
import wolfsoft1.pay2wallet.R;

public class CustomAdapter implements ListAdapter {

    ArrayList<Absence_details> arrayList;
    Context context;
    DBManager manager;

    public CustomAdapter(Context context, ArrayList<Absence_details> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
        manager = new DBManager(context);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {


    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Absence_details subjectData=arrayList.get(position);
        if(convertView==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.layout_custom_scan, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            customfonts.MyTextView_Roboto_Regular nom = (customfonts.MyTextView_Roboto_Regular) convertView.findViewById(R.id.nom);
            customfonts.MyTextView_Roboto_Regular CNE = (customfonts.MyTextView_Roboto_Regular) convertView.findViewById(R.id.CNE);

            Etudiant etudiant = getEtudiantsbyCne(subjectData.getCNE()).get(0);
            nom.setText(etudiant.getNom()+" "+etudiant.getPrenom());
            CNE.setText(subjectData.getCNE());
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
    public List<Etudiant> getEtudiantsbyCne(String CNE){
        return  manager.getEtudiantByCNE(CNE);

    }
}