ALTER TABLE car
    ALTER COLUMN registration_timestamp SET DATA TYPE timestamp with time zone
        USING
            timestamp with time zone 'epoch' + registration_timestamp * interval '1 second';