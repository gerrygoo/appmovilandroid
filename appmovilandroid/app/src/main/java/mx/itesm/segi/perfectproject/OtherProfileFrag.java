package mx.itesm.segi.perfectproject;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Model.Model;
import Model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherProfileFrag extends Fragment {

    public final static String ARG_USER = "user";
    public final static String ARG_JOINED = "joined";

    private ImageView ivProfile;
    private EditText tvName;
    private EditText tvCompany;
    private EditText rateNum;
    private RatingBar rbRating;
    private EditText tvEmail;
    private EditText tvSkills;

    private User user;

    public OtherProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadUser();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadUser() {
        user = getArguments().getParcelable(ARG_USER);

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    user.setRating( Tasks.await(Model.getInstance().getRating(user.getUID()) ) );
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }

        }.execute();

        ivProfile = getActivity().findViewById(R.id.ivProfile);
        tvName = getActivity().findViewById(R.id.tvName);
        tvCompany = getActivity().findViewById(R.id.tvCompany);
        rateNum = getActivity().findViewById(R.id.rateNumber);
        rbRating = getActivity().findViewById(R.id.rbRating);
        tvEmail = getActivity().findViewById(R.id.tvEmail);
        tvSkills = getActivity().findViewById(R.id.tvSkills);

        user.setImageListener(new ImageListener() {
            @Override
            public void onImageAvailable(Bitmap image) {
                ivProfile.setImageBitmap(image);
            }
        });
        tvName.setText(user.getName());
        tvCompany.setText(user.getCompany());
        rateNum.setText(user.getRating() + "");
        rbRating.setRating((float) user.getRating());

        if(getArguments().getBoolean(ARG_JOINED)) tvEmail.setText(user.getEmail());
        else tvEmail.setText(R.string.email_not_available);

        ArrayList<String> skills = user.getSkills();
        String skillsText = "";
        for(int i = 0;i < skills.size();i++)
        {
            skillsText += skills.get(i);
            if(i != skills.size()-1) skillsText += " ";
        }
        tvSkills.setText(skillsText);
    }

}
