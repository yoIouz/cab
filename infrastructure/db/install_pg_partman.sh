#!/bin/bash

psql -U postgres -d account -c "CREATE EXTENSION IF NOT EXISTS pg_partman;"