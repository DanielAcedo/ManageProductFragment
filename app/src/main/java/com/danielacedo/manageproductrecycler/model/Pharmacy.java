package com.danielacedo.manageproductrecycler.model;

import java.util.Comparator;

/**
 * Created by usuario on 11/01/17.
 */

public class Pharmacy {
    private int id;
    private String cif;
    private String address;
    private String telephone_number;
    private String email;

    public static final Comparator<Pharmacy> NAME_ASCENDANT_COMPARATOR =
            new Comparator<Pharmacy>() {
                @Override
                public int compare(Pharmacy o1, Pharmacy o2) {
                    return o1.getCif().compareTo(o2.getCif());
                }
            };

    public static final Comparator<Pharmacy> NAME_DESCENDANT_COMPARATOR =
            new Comparator<Pharmacy>() {
                @Override
                public int compare(Pharmacy o1, Pharmacy o2) {
                    return -1 * NAME_ASCENDANT_COMPARATOR.compare(o1, o2);
                }
            };


    public Pharmacy(){}

    public Pharmacy(int id, String cif, String address, String telephone_number, String email) {
        this.id = id;
        this.cif = cif;
        this.address = address;
        this.telephone_number = telephone_number;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getTelephone_number() {
        return telephone_number;
    }

    public void setTelephone_number(String telephone_number) {
        this.telephone_number = telephone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
