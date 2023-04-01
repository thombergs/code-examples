package com.reflectoring.userdetails.persistence;

public class Views {
    public static interface ExternalView {

    }
    public static interface InternalView extends ExternalView {

    }

    public static interface UserSummary {

    }

    public static interface UserDetailedSummary extends UserSummary {

    }

    public static interface GetView {

    }

    public static interface PatchView {

    }
}
