--liquibase formatted sql
--changeset Dmitry:4 splitStatements:false

DO '
    DECLARE
        tbl TEXT := ''public.transactions'';
        created BOOLEAN;
        result TEXT;
    BEGIN
        SELECT public.create_parent(
                       p_parent_table := tbl,
                       p_control := ''transaction_date'',
                       p_interval := ''1 month'',
                       p_type := ''range'',
                       p_premake := ''1''
               ) INTO result;

        IF result IS NULL OR result = '''' THEN
            RAISE EXCEPTION ''PARTITIONING FAILED'';
        END IF;

        SELECT EXISTS (
            SELECT 1 FROM public.part_config
            WHERE parent_table = tbl
        ) INTO created;

        IF NOT created THEN
            RAISE EXCEPTION ''PARTITIONING CONFIG WAS NOT CREATED FOR TABLE %'', tbl;
        END IF;
    END; '
