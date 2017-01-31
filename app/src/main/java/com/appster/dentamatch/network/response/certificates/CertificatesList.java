package com.appster.dentamatch.network.response.certificates;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by virender on 14/01/17.
 */
public class CertificatesList implements Parcelable {
    private int id;
    private String certificateName;
    @SerializedName("imagePath")
    private String image;
    private String imageUrl;
    @SerializedName("validityDate")
    private String validityDate;
    private boolean isImageUploaded;

    private CertificatesList(Parcel in) {
        id = in.readInt();
        certificateName = in.readString();
        image = in.readString();
        imageUrl = in.readString();
        validityDate = in.readString();
        isImageUploaded = in.readByte() != 0;
    }

    public static final Creator<CertificatesList> CREATOR = new Creator<CertificatesList>() {
        @Override
        public CertificatesList createFromParcel(Parcel in) {
            return new CertificatesList(in);
        }

        @Override
        public CertificatesList[] newArray(int size) {
            return new CertificatesList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(certificateName);
        parcel.writeString(image);
        parcel.writeString(imageUrl);
        parcel.writeString(validityDate);
        parcel.writeByte((byte) (isImageUploaded ? 1 : 0));
    }

    public boolean isImageUploaded() {
        return isImageUploaded;
    }

    public void setImageUploaded(boolean imageUploaded) {
        isImageUploaded = imageUploaded;
    }

    public String getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(String validityDate) {
        this.validityDate = validityDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
