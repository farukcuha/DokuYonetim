package com.example.dokuyonetim.Activitys;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.dokuyonetim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class YazıdegistirmeDialog extends AppCompatDialogFragment {
    private AlertDialog.Builder builder;
    private EditText editText;
    private ProgressBar pd;
    private FirebaseFirestore firestore;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.anasayfayazidialg, null);
        editText = view.findViewById(R.id.yazigirmedialog);
        pd = view.findViewById(R.id.pd);
        firestore = FirebaseFirestore.getInstance();




        builder.setView(view).setTitle("Ana Sayfa Yazısı")
                .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.setVisibility(View.VISIBLE);
                String str_yazi = editText.getText().toString();


                if(TextUtils.isEmpty(str_yazi)){
                    Toast.makeText(getContext(), "Alanı doldurmak istemiyorsanız iptale basabilirsiniz.", Toast.LENGTH_SHORT).show();

                }
                else {
                    firestore.collection("Anasayfa Itemleri").document("İtemler")
                            .update("metin", str_yazi).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                pd.setVisibility(View.GONE);
                            }
                            else{
                                Toast.makeText(getContext(), "Alanı doldurmak istemiyorsanız iptale basabilirsiniz.", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }





            }
        });

        return builder.create();


    }
}
