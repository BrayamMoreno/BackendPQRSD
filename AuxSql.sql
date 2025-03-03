CREATE OR REPLACE FUNCTION registrar_modificacion_pq() RETURNS TRIGGER AS $$
DECLARE
    columna TEXT;
    valor_old TEXT;
    valor_new TEXT;
BEGIN
    -- Iterar sobre todas las columnas de la tabla "pqs"
    FOR columna IN 
        SELECT column_name FROM information_schema.columns 
        WHERE table_name = 'pqs' 
    LOOP
        -- Obtener los valores antes y después del cambio
        EXECUTE format('SELECT ($1).%I::TEXT, ($2).%I::TEXT', columna, columna)
        INTO valor_old, valor_new
        USING OLD, NEW;
        
        -- Si el valor cambió, lo registramos en el historial
        IF valor_old IS DISTINCT FROM valor_new THEN
            INSERT INTO historial_modificaciones_pq (pq_id, usuario_id, fecha_modificacion, campo_modificado, valor_anterior, valor_nuevo)
            VALUES (NEW.id, NEW.modificador_id, now(), columna, valor_old, valor_new);
        END IF;
    END LOOP;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_historial_pq
BEFORE UPDATE ON pqs
FOR EACH ROW
EXECUTE FUNCTION registrar_modificacion_pq();


SELECT hmp.fecha_modificacion, u.username, hmp.campo_modificado, hmp.valor_anterior, hmp.valor_nuevo
FROM historial_modificaciones_pq hmp
JOIN usuarios u ON hmp.usuario_id = u.id
WHERE hmp.pq_id = 123
ORDER BY hmp.fecha_modificacion DESC;
