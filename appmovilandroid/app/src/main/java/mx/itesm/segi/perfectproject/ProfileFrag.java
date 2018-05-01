package mx.itesm.segi.perfectproject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;

import java.io.FileNotFoundException;
import java.io.InputStream;

import Model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFrag extends Fragment {

    public final static String ARG_USER = "user";
    public final static String ARG_MODE = "mode";
    private final static int PICK_PHOTO_FOR_AVATAR=1;

    private int numberOfLines=0;
    private EditText tvName;
    private EditText tvCompany;
    private EditText tvCurriculum;
    private EditText tvSkill;
    private ImageButton editProfilePicture;
    private EditText rateNum;
    private RatingBar rbRating;
    private Switch Mode;
    private OnSwitchToggleListener listener;
    private ImageView ivProfile;
    private LinearLayout skillLayout;
    private Button addSkill;

    private User user;

    public ProfileFrag() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if(activity instanceof OnSwitchToggleListener){
            listener = (OnSwitchToggleListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement OnSwitchToggleListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = v.findViewById(R.id.tvName);
        tvCompany = v.findViewById(R.id.tvCompany);
        //tvCurriculum = v.findViewById(R.id.etCurriculum);
        editProfilePicture = v.findViewById(R.id.editProfilePic);
        rbRating = v.findViewById(R.id.rbRating);
        ivProfile=v.findViewById(R.id.ivProfile);
        rateNum = v.findViewById(R.id.rateNumber);
        addSkill = v.findViewById(R.id.btnAddSkill);
        tvSkill = v.findViewById(R.id.etSkills);

//        tvCurriculum.setKeyListener(null);
        Mode = v.findViewById(R.id.sEmployer);
        Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listener.OnSwitchToggle(b);
            }
        });

        tvCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                user.setCompany(tvCompany.getText().toString());
            }
        });

        //Para poder editar la barra y guardar el valor del rate
        /*rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                user.setRating(v);
            }
        });*/

        editProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfilePic(view);
            }
        });

        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add_Line(v);
            }
        });

        loadProfileInfo();
        return v;
    }

    public void Add_Line(View v) {
        LinearLayout ll = v.findViewById(R.id.SkillsLayout);
        // add edittext
        EditText et = new EditText(getActivity().getApplicationContext());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(p);
        et.setHint(tvSkill.getHint().toString());
        et.setId(numberOfLines + 1);
        ll.addView(et);
        numberOfLines++;
    }

    public void setProfilePic(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap BMimage = BitmapFactory.decodeStream(inputStream);
                ivProfile.setImageBitmap(BMimage);
                user.setProfPic(BMimage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
        else if(resultCode==Activity.RESULT_CANCELED){
            //No se eligió imagen de Profile Pic.
        }
    }

    private void loadProfileInfo() {
        user = getArguments().getParcelable(ARG_USER);
        ivProfile.setImageBitmap(user.getProfPic());
        tvCompany.setText(user.getCompany());
        //tvCurriculum.setText(user.getSkills().toString());
        tvName.setText(user.getName());
        rbRating.setRating(user.getRating());
        rateNum.setText(String.valueOf(user.getRating()));
        Mode.setChecked(getArguments().getBoolean(ARG_MODE));
    }

    public interface OnSwitchToggleListener {
        void OnSwitchToggle(boolean value);
    }
}
