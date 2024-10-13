package com.example.cmaisonneuve;

import java.io.Serializable;

public class User implements Serializable {
///////////////////////1
        private int id; // Campo para el ID del usuario
    ///////////////////////1

    private String fullname;
        private String username;
        private String password;
///////////////////////1

        public int getId(){
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
///////////////////////1

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

