package com.thoughtworks.contraman;

public enum EventType {

    LUNCH {
        @Override
        public String toString() {
            return "Lunch";
        }
    }, SESSION, NETWORKING {
        @Override
        public String toString() {
            return "Networking Event";
        }
    }


}
