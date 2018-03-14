package mx.itesm.ins.proyectomovilandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * ProjectCard.OnFragmentInteractionListener interface
 * to handle interaction events.
 * Use the {@link ProjectCard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectCard extends Fragment {
    public static final String ARG_PROJECT = "Project";

    //Model
    private Project project;

    //View
    private TextView Title;
    private ImageView Logo;
    private TextView StartDate;
    private TextView Duration;
    private TextView Positions;
    private TextView Location;
    private Button Accept;
    private Button Decline;
    private ProgressBar progressBar;

    private boolean shouldPutImage;


    public ProjectCard() {
        // Required empty public constructor
    }

    public static ProjectCard newInstance(Project project) {
        ProjectCard fragment = new ProjectCard();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROJECT, project);
        fragment.shouldPutImage = false;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = getArguments().getParcelable(ARG_PROJECT);
            project.setImageListener(new ImageListener() {
                @Override
                public void onImageAvailable(Bitmap image) {
                    if(Logo ==  null){
                        shouldPutImage = true;
                        return;
                    }
                    Logo.setImageBitmap(image);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_project_card, container, false);
        // Inflate the layout for this

        Title = result.findViewById(R.id.projectCard_Title);;
        Logo = result.findViewById(R.id.projectCard_Logo);
        StartDate = result.findViewById(R.id.projectCart_StartDate);
        Duration = result.findViewById(R.id.projectCard_Duration);
        Positions = result.findViewById(R.id.projectCard_Positions);
        Location = result.findViewById(R.id.projectCard_Location);
        Accept = result.findViewById(R.id.projectCard_Accept);
        Decline = result.findViewById(R.id.projectCard_Decline);
        progressBar = result.findViewById(R.id.projectCard_ProgressBar);

        InitializeCard();
        return result;
    }

    private void InitializeCard() {
        Title.setText(project.getTitle());
        if(shouldPutImage) {
            Logo.setImageBitmap(project.getImage());
            progressBar.setVisibility(View.GONE);
        }
        StartDate.setText(DateFormat.getDateFormat(getContext()).format(project.getStartDate()));
        Duration.setText(DateFormat.getDateFormat(getContext()).format(project.getEndDate()));
        StringBuilder positions = new StringBuilder();
        String[] projectPositions = project.getPositions();
        for(int i = 0; i < projectPositions.length; i++){
            positions.append(projectPositions[i]);
            if(i != projectPositions.length - 1) {
                positions.append("\n");
            }
        }
        Positions.setText(positions.toString());
        Location.setText(project.getLocation());

    }
}