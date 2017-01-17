package com.appster.dentamatch.network.response.affiliation;

/**
     * Created by virender on 16/01/17.
     */
    public  class AffiliationData {
        private int affiliationId;


        private int jobSeekerAffiliationStatus;
        private String affiliationName;
        private String otherAffiliation;

        public int getAffiliationId() {
            return affiliationId;
        }

        public int getJobSeekerAffiliationStatus() {
            return jobSeekerAffiliationStatus;
        }

        public void setJobSeekerAffiliationStatus(int jobSeekerAffiliationStatus) {
            this.jobSeekerAffiliationStatus = jobSeekerAffiliationStatus;
        }

        public void setAffiliationId(int affiliationId) {
            this.affiliationId = affiliationId;
        }

        public String getAffiliationName() {
            return affiliationName;
        }

        public void setAffiliationName(String affiliationName) {
            this.affiliationName = affiliationName;
        }

        public String getOtherAffiliation() {
            return otherAffiliation;
        }

        public void setOtherAffiliation(String otherAffiliation) {
            this.otherAffiliation = otherAffiliation;
        }


    }