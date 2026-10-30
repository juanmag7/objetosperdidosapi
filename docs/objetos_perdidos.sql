--
-- PostgreSQL database dump
--

\restrict O0rWzA3l9hknGO6ZG6om6ltbsZUsu7F7WkGTuIdCnyNi7wfliD0fg4Je6dxuJg1

-- Dumped from database version 18.6
-- Dumped by pg_dump version 18.6

-- Started on 2026-10-22 19:41:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 227 (class 1259 OID 16478)
-- Name: decisiones_reclamacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.decisiones_reclamacion (
    id bigint NOT NULL,
    reclamacion_id bigint NOT NULL,
    decidido_por_id bigint NOT NULL,
    decision character varying(20) NOT NULL,
    observacion text,
    fecha_decision timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT decisiones_reclamacion_decision_check CHECK (((decision)::text = ANY ((ARRAY['APROBADA'::character varying, 'RECHAZADA'::character varying])::text[])))
);


ALTER TABLE public.decisiones_reclamacion OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16477)
-- Name: decisiones_reclamacion_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.decisiones_reclamacion ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.decisiones_reclamacion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 223 (class 1259 OID 16432)
-- Name: detalles_verificacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detalles_verificacion (
    hallazgo_id bigint NOT NULL,
    descripcion_verificacion_privada text NOT NULL,
    fecha_actualizacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.detalles_verificacion OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16406)
-- Name: hallazgos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hallazgos (
    id bigint NOT NULL,
    registrado_por_id bigint NOT NULL,
    titulo character varying(120) NOT NULL,
    categoria character varying(60) NOT NULL,
    descripcion_publica text NOT NULL,
    lugar_hallazgo character varying(160) NOT NULL,
    fecha_hallazgo timestamp without time zone NOT NULL,
    estado character varying(20) DEFAULT 'REGISTRADO'::character varying NOT NULL,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT hallazgos_estado_check CHECK (((estado)::text = ANY ((ARRAY['REGISTRADO'::character varying, 'PUBLICADO'::character varying, 'ENTREGADO'::character varying])::text[])))
);


ALTER TABLE public.hallazgos OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16405)
-- Name: hallazgos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.hallazgos ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.hallazgos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 225 (class 1259 OID 16449)
-- Name: reclamaciones; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reclamaciones (
    id bigint NOT NULL,
    hallazgo_id bigint NOT NULL,
    reclamante_id bigint NOT NULL,
    evidencia_privada text NOT NULL,
    estado character varying(20) DEFAULT 'PENDIENTE'::character varying NOT NULL,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT reclamaciones_estado_check CHECK (((estado)::text = ANY ((ARRAY['PENDIENTE'::character varying, 'APROBADA'::character varying, 'RECHAZADA'::character varying, 'RETIRADA'::character varying, 'CERRADA'::character varying])::text[])))
);


ALTER TABLE public.reclamaciones OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16448)
-- Name: reclamaciones_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.reclamaciones ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.reclamaciones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 16389)
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios (
    id bigint NOT NULL,
    nombre character varying(120) NOT NULL,
    correo character varying(180) NOT NULL,
    codigo_institucional character varying(40),
    rol character varying(20) NOT NULL,
    activo boolean DEFAULT true NOT NULL,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT usuarios_rol_check CHECK (((rol)::text = ANY ((ARRAY['COMUNIDAD'::character varying, 'SEGURIDAD'::character varying])::text[])))
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16388)
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.usuarios ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.usuarios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 5070 (class 0 OID 16478)
-- Dependencies: 227
-- Data for Name: decisiones_reclamacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.decisiones_reclamacion (id, reclamacion_id, decidido_por_id, decision, observacion, fecha_decision) FROM stdin;
1	1	2	APROBADA	La evidencia presentada coincide con el objeto encontrado.	2026-10-22 17:50:09.574942
\.


--
-- TOC entry 5066 (class 0 OID 16432)
-- Dependencies: 223
-- Data for Name: detalles_verificacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detalles_verificacion (hallazgo_id, descripcion_verificacion_privada, fecha_actualizacion) FROM stdin;
1	La mochila tiene un compartimento interno y un llavero azul.	2026-10-22 17:52:41.384454
\.


--
-- TOC entry 5065 (class 0 OID 16406)
-- Dependencies: 222
-- Data for Name: hallazgos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hallazgos (id, registrado_por_id, titulo, categoria, descripcion_publica, lugar_hallazgo, fecha_hallazgo, estado, fecha_creacion, fecha_actualizacion) FROM stdin;
1	1	Mochila negra	Accesorios	Mochila negra encontrada en las instalaciones.	Biblioteca	2026-10-22 17:47:04.907408	REGISTRADO	2026-10-22 17:47:04.907408	2026-10-22 17:47:04.907408
\.


--
-- TOC entry 5068 (class 0 OID 16449)
-- Dependencies: 225
-- Data for Name: reclamaciones; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reclamaciones (id, hallazgo_id, reclamante_id, evidencia_privada, estado, fecha_creacion, fecha_actualizacion) FROM stdin;
1	1	1	La mochila tiene un llavero azul en el bolsillo lateral.	APROBADA	2026-10-22 17:48:42.729038	2026-10-22 17:51:24.130729
\.


--
-- TOC entry 5063 (class 0 OID 16389)
-- Dependencies: 220
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuarios (id, nombre, correo, codigo_institucional, rol, activo, fecha_creacion) FROM stdin;
1	Juan Perez	juan.prueba@institucion.edu.co	EST001	COMUNIDAD	t	2026-10-22 17:45:03.834516
2	Maria Gomez	maria.seguridad@institucion.edu.co	SEG001	SEGURIDAD	t	2026-10-22 17:45:03.834516
\.


--
-- TOC entry 5076 (class 0 OID 0)
-- Dependencies: 226
-- Name: decisiones_reclamacion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.decisiones_reclamacion_id_seq', 1, true);


--
-- TOC entry 5077 (class 0 OID 0)
-- Dependencies: 221
-- Name: hallazgos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hallazgos_id_seq', 1, true);


--
-- TOC entry 5078 (class 0 OID 0)
-- Dependencies: 224
-- Name: reclamaciones_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reclamaciones_id_seq', 1, true);


--
-- TOC entry 5079 (class 0 OID 0)
-- Dependencies: 219
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 5, true);


--
-- TOC entry 4905 (class 2606 OID 16491)
-- Name: decisiones_reclamacion decisiones_reclamacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.decisiones_reclamacion
    ADD CONSTRAINT decisiones_reclamacion_pkey PRIMARY KEY (id);


--
-- TOC entry 4907 (class 2606 OID 16493)
-- Name: decisiones_reclamacion decisiones_reclamacion_reclamacion_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.decisiones_reclamacion
    ADD CONSTRAINT decisiones_reclamacion_reclamacion_id_key UNIQUE (reclamacion_id);


--
-- TOC entry 4898 (class 2606 OID 16442)
-- Name: detalles_verificacion detalles_verificacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalles_verificacion
    ADD CONSTRAINT detalles_verificacion_pkey PRIMARY KEY (hallazgo_id);


--
-- TOC entry 4894 (class 2606 OID 16426)
-- Name: hallazgos hallazgos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hallazgos
    ADD CONSTRAINT hallazgos_pkey PRIMARY KEY (id);


--
-- TOC entry 4903 (class 2606 OID 16466)
-- Name: reclamaciones reclamaciones_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reclamaciones
    ADD CONSTRAINT reclamaciones_pkey PRIMARY KEY (id);


--
-- TOC entry 4890 (class 2606 OID 16404)
-- Name: usuarios usuarios_correo_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_correo_key UNIQUE (correo);


--
-- TOC entry 4892 (class 2606 OID 16402)
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- TOC entry 4908 (class 1259 OID 16509)
-- Name: idx_decisiones_decidido_por; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_decisiones_decidido_por ON public.decisiones_reclamacion USING btree (decidido_por_id);


--
-- TOC entry 4895 (class 1259 OID 16505)
-- Name: idx_hallazgos_estado; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_hallazgos_estado ON public.hallazgos USING btree (estado);


--
-- TOC entry 4896 (class 1259 OID 16504)
-- Name: idx_hallazgos_registrado_por; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_hallazgos_registrado_por ON public.hallazgos USING btree (registrado_por_id);


--
-- TOC entry 4899 (class 1259 OID 16508)
-- Name: idx_reclamaciones_estado; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_reclamaciones_estado ON public.reclamaciones USING btree (estado);


--
-- TOC entry 4900 (class 1259 OID 16506)
-- Name: idx_reclamaciones_hallazgo; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_reclamaciones_hallazgo ON public.reclamaciones USING btree (hallazgo_id);


--
-- TOC entry 4901 (class 1259 OID 16507)
-- Name: idx_reclamaciones_reclamante; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_reclamaciones_reclamante ON public.reclamaciones USING btree (reclamante_id);


--
-- TOC entry 4913 (class 2606 OID 16494)
-- Name: decisiones_reclamacion fk_decision_reclamacion; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.decisiones_reclamacion
    ADD CONSTRAINT fk_decision_reclamacion FOREIGN KEY (reclamacion_id) REFERENCES public.reclamaciones(id);


--
-- TOC entry 4914 (class 2606 OID 16499)
-- Name: decisiones_reclamacion fk_decision_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.decisiones_reclamacion
    ADD CONSTRAINT fk_decision_usuario FOREIGN KEY (decidido_por_id) REFERENCES public.usuarios(id);


--
-- TOC entry 4909 (class 2606 OID 16427)
-- Name: hallazgos fk_hallazgo_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hallazgos
    ADD CONSTRAINT fk_hallazgo_usuario FOREIGN KEY (registrado_por_id) REFERENCES public.usuarios(id);


--
-- TOC entry 4911 (class 2606 OID 16467)
-- Name: reclamaciones fk_reclamacion_hallazgo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reclamaciones
    ADD CONSTRAINT fk_reclamacion_hallazgo FOREIGN KEY (hallazgo_id) REFERENCES public.hallazgos(id);


--
-- TOC entry 4912 (class 2606 OID 16472)
-- Name: reclamaciones fk_reclamacion_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reclamaciones
    ADD CONSTRAINT fk_reclamacion_usuario FOREIGN KEY (reclamante_id) REFERENCES public.usuarios(id);


--
-- TOC entry 4910 (class 2606 OID 16443)
-- Name: detalles_verificacion fk_verificacion_hallazgo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalles_verificacion
    ADD CONSTRAINT fk_verificacion_hallazgo FOREIGN KEY (hallazgo_id) REFERENCES public.hallazgos(id) ON DELETE CASCADE;


-- Completed on 2026-10-22 19:41:12

--
-- PostgreSQL database dump complete
--

\unrestrict O0rWzA3l9hknGO6ZG6om6ltbsZUsu7F7WkGTuIdCnyNi7wfliD0fg4Je6dxuJg1

