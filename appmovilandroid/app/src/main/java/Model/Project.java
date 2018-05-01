package Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import mx.itesm.segi.perfectproject.ImageListener;

/**
 * Created by ianne on 28/02/2018.
 */

public class Project implements Parcelable{

    private String UID;
    private User Owner;
    private String Title;
    private Bitmap Image;
    private String ImageUrl;
    private String[] Positions;
    private String Description;
    private String Location;
    private Date StartDate;
    private Date EndDate;
    private double Compensation;

    private ArrayList<User> Applicants;
    private ArrayList<User> Team;

    private ImageListener imageListener;

    private Boolean finishLoadingImage;

    //private ImageReader.OnImageAvailableListener

    public Project(String uid, User owner, String title, String imageUrl, String[] positions, String description,String location, Date startDate, Date endDate) {
        UID = uid;
        Owner = owner;
        Title = title;
        ImageUrl = imageUrl;
        Positions = positions;
        Description = description;
        Location = location;
        StartDate = startDate;
        EndDate = endDate;

        Team = new ArrayList<>();
        Applicants = new ArrayList<>();

        finishLoadingImage = false;
        new Project.DownloadImageFromURL().execute(imageUrl);
    }

    public Project(String uid, User owner, String title, Bitmap image, String[] positions, String description,String location, Date startDate, Date endDate) {
        UID = uid;
        Owner = owner;
        Title = title;
        Image = image;
        Positions = positions;
        Description = description;
        Location = location;
        StartDate = startDate;
        EndDate = endDate;

        Team = new ArrayList<>();
        Applicants = new ArrayList<>();

        finishLoadingImage = true;
        //new Project.DownloadImageFromIS().execute(inputStream);
    }

    protected Project(Parcel in) {
        Title = in.readString();
        ImageUrl = in.readString();
        Positions = in.createStringArray();
        Description = in.readString();
        Location = in.readString();
        StartDate = new Date(in.readLong());
        EndDate = new Date(in.readLong());
        finishLoadingImage = false;

        new Project.DownloadImageFromURL().execute(ImageUrl);
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromIS(InputStream is) {
        Bitmap myBitmap = BitmapFactory.decodeStream(is);
        return myBitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Title);
        parcel.writeString(ImageUrl);
        parcel.writeArray(Positions);
        parcel.writeString(Description);
        parcel.writeString(Location);
        parcel.writeLong(StartDate.getTime());
        parcel.writeLong(EndDate.getTime());
    }

    private class DownloadImageFromURL extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            return getBitmapFromURL(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Image = bitmap;
            finishLoadingImage = true;
            if(imageListener != null){
                imageListener.onImageAvailable(bitmap);
            }
        }
    }

    private class DownloadImageFromIS extends AsyncTask<InputStream, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(InputStream... inputStreams) {
            return getBitmapFromIS(inputStreams[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Image = bitmap;
            finishLoadingImage = true;
            if(imageListener != null){
                imageListener.onImageAvailable(bitmap);
            }
        }
    }

    public void setImageListener(ImageListener imageListener){
        this.imageListener = imageListener;
        if(finishLoadingImage){
            imageListener.onImageAvailable(this.Image);
        }
    }
    public String getUID() {
        return UID;
    }

    void setUID(String UID) {
        this.UID = UID;
    }

    public String getTitle() {
        return Title;
    }

    public Bitmap getImage() {
        return Image;
    }

    public String[] getPositions() {
        return Positions;
    }

    public String getDescription() {
        return Description;
    }

    public String getLocation() {
        return Location;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public double getCompensation() {
        return Compensation;
    }

    public ArrayList<User> getApplicants() {
        return Applicants;
    }

    protected void setApplicants(ArrayList<User> applicants) {
        Applicants = applicants;
    }

    public void addApplicant(User applicant){
        Applicants.add(applicant);
    }

    public void removeApplicant(User applicant) {
        Applicants.remove(applicant);
    }

    public ArrayList<User> getTeam() {
        return Team;
    }

    protected void setTeam(ArrayList<User> team) {
        Team = team;
    }

    public void addTeamMember(User user){
        this.Team.add(user);
    }

    public void removeTeamMember(User user){
        this.Team.remove(user);
    }

    public User getOwner() {
        return Owner;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Project){
            if(((Project) obj).UID == UID){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return UID.hashCode();
    }
}
