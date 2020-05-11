--
-- PostgreSQL database dump
--

-- Dumped from database version 12.2
-- Dumped by pg_dump version 12.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: genre; Type: TABLE; Schema: public; Owner: developers
--

CREATE TABLE public.genre (
    genre_id integer NOT NULL,
    genre character varying(50)
);


ALTER TABLE public.genre OWNER TO developers;

--
-- Name: genre_filmed; Type: TABLE; Schema: public; Owner: developers
--

CREATE TABLE public.genre_filmed (
    genre_id integer NOT NULL,
    film_id uuid NOT NULL
);


ALTER TABLE public.genre_filmed OWNER TO developers;

--
-- Name: genre_filmed_genre_id_seq; Type: SEQUENCE; Schema: public; Owner: developers
--

CREATE SEQUENCE public.genre_filmed_genre_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.genre_filmed_genre_id_seq OWNER TO developers;

--
-- Name: genre_filmed_genre_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developers
--

ALTER SEQUENCE public.genre_filmed_genre_id_seq OWNED BY public.genre_filmed.genre_id;


--
-- Name: genre_genre_id_seq; Type: SEQUENCE; Schema: public; Owner: developers
--

CREATE SEQUENCE public.genre_genre_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.genre_genre_id_seq OWNER TO developers;

--
-- Name: genre_genre_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developers
--

ALTER SEQUENCE public.genre_genre_id_seq OWNED BY public.genre.genre_id;


--
-- Name: movies; Type: TABLE; Schema: public; Owner: developers
--

CREATE TABLE public.movies (
    title character varying,
    id uuid NOT NULL,
    release_year integer NOT NULL,
    poster character varying(255)
);


ALTER TABLE public.movies OWNER TO developers;

--
-- Name: movies_release_year_seq; Type: SEQUENCE; Schema: public; Owner: developers
--

CREATE SEQUENCE public.movies_release_year_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.movies_release_year_seq OWNER TO developers;

--
-- Name: movies_release_year_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developers
--

ALTER SEQUENCE public.movies_release_year_seq OWNED BY public.movies.release_year;


--
-- Name: person; Type: TABLE; Schema: public; Owner: developers
--

CREATE TABLE public.person (
    person_id uuid NOT NULL,
    person_name character varying(50) NOT NULL,
    birthday date,
    birthplace character varying(50)
);


ALTER TABLE public.person OWNER TO developers;

--
-- Name: profession; Type: TABLE; Schema: public; Owner: developers
--

CREATE TABLE public.profession (
    profession_id integer NOT NULL,
    profession_name character varying(50) NOT NULL
);


ALTER TABLE public.profession OWNER TO developers;

--
-- Name: proffesion_proffesion_id_seq; Type: SEQUENCE; Schema: public; Owner: developers
--

CREATE SEQUENCE public.proffesion_proffesion_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.proffesion_proffesion_id_seq OWNER TO developers;

--
-- Name: proffesion_proffesion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developers
--

ALTER SEQUENCE public.proffesion_proffesion_id_seq OWNED BY public.profession.profession_id;


--
-- Name: worked_on; Type: TABLE; Schema: public; Owner: developers
--

CREATE TABLE public.worked_on (
    person_id uuid NOT NULL,
    profession_id integer NOT NULL,
    film_id uuid NOT NULL,
    role character varying(50)
);


ALTER TABLE public.worked_on OWNER TO developers;

--
-- Name: worked_on_proffesion_id_seq; Type: SEQUENCE; Schema: public; Owner: developers
--

CREATE SEQUENCE public.worked_on_proffesion_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.worked_on_proffesion_id_seq OWNER TO developers;

--
-- Name: worked_on_proffesion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developers
--

ALTER SEQUENCE public.worked_on_proffesion_id_seq OWNED BY public.worked_on.profession_id;


--
-- Name: genre genre_id; Type: DEFAULT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.genre ALTER COLUMN genre_id SET DEFAULT nextval('public.genre_genre_id_seq'::regclass);


--
-- Name: genre_filmed genre_id; Type: DEFAULT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.genre_filmed ALTER COLUMN genre_id SET DEFAULT nextval('public.genre_filmed_genre_id_seq'::regclass);


--
-- Name: movies release_year; Type: DEFAULT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.movies ALTER COLUMN release_year SET DEFAULT nextval('public.movies_release_year_seq'::regclass);


--
-- Name: profession profession_id; Type: DEFAULT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.profession ALTER COLUMN profession_id SET DEFAULT nextval('public.proffesion_proffesion_id_seq'::regclass);


--
-- Name: worked_on profession_id; Type: DEFAULT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.worked_on ALTER COLUMN profession_id SET DEFAULT nextval('public.worked_on_proffesion_id_seq'::regclass);


--
-- Data for Name: genre; Type: TABLE DATA; Schema: public; Owner: developers
--

COPY public.genre (genre_id, genre) FROM stdin;
1	Comedy
2	Sci-Fi
3	Horror
4	Romance
5	Action
6	Thriller
7	Drama
8	Mystery
9	Crime
10	Animation
11	Adventure
12	Fantasy
13	Comedy-Romance
14	Action-Comedy
15	Superhero
16	Biography
17	Music
18	War
19	Western
20	Sport
21	Film Noir
22	History
23	Short Film
24	Family
\.


--
-- Data for Name: genre_filmed; Type: TABLE DATA; Schema: public; Owner: developers
--

COPY public.genre_filmed (genre_id, film_id) FROM stdin;
\.


--
-- Data for Name: movies; Type: TABLE DATA; Schema: public; Owner: developers
--

COPY public.movies (title, id, release_year, poster) FROM stdin;
Batman: The Killing Joke	6927c741-f334-41ae-a3b8-e74935fdc464	2016	https://m.media-amazon.com/images/M/MV5BMTdjZTliODYtNWExMi00NjQ1LWIzN2MtN2Q5NTg5NTk3NzliL2ltYWdlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SX300.jpg
Batman v Superman: Dawn of Justice	6e0b9fc1-76dd-4b4f-99e2-72b0c09cb9dc	2016	https://m.media-amazon.com/images/M/MV5BYThjYzcyYzItNTVjNy00NDk0LTgwMWQtYjMwNmNlNWJhMzMyXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg
Batman	78b24806-c8e0-4766-b9d6-d084d02690d9	1989	https://m.media-amazon.com/images/M/MV5BMTYwNjAyODIyMF5BMl5BanBnXkFtZTYwNDMwMDk2._V1_SX300.jpg
Batman Returns	3727ba83-253f-4234-9eea-7fadf6f9e872	1992	https://m.media-amazon.com/images/M/MV5BOGZmYzVkMmItM2NiOS00MDI3LWI4ZWQtMTg0YWZkODRkMmViXkEyXkFqcGdeQXVyODY0NzcxNw@@._V1_SX300.jpg
Batman Forever	d289746d-1754-4f89-b74a-0ef3c237f5b1	1995	https://m.media-amazon.com/images/M/MV5BNDdjYmFiYWEtYzBhZS00YTZkLWFlODgtY2I5MDE0NzZmMDljXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg
Batman & Robin	26148241-d6f9-4cb3-b0c4-507f9f1e1eb7	1997	https://m.media-amazon.com/images/M/MV5BMGQ5YTM1NmMtYmIxYy00N2VmLWJhZTYtN2EwYTY3MWFhOTczXkEyXkFqcGdeQXVyNTA2NTI0MTY@._V1_SX300.jpg
Batman: Under the Red Hood	aaa3fc68-552f-4dbc-beed-db1d9adc9d95	2010	https://m.media-amazon.com/images/M/MV5BNmY4ZDZjY2UtOWFiYy00MjhjLThmMjctOTQ2NjYxZGRjYmNlL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_SX300.jpg
Batman: The Dark Knight Returns, Part 1	092a04be-87e2-4f3e-974b-ff2a94b26833	2012	https://m.media-amazon.com/images/M/MV5BMzIxMDkxNDM2M15BMl5BanBnXkFtZTcwMDA5ODY1OQ@@._V1_SX300.jpg
The Lego Batman Movie	1d0af5b5-60d3-4b53-a2e3-bf8847744de1	2017	https://m.media-amazon.com/images/M/MV5BMTcyNTEyOTY0M15BMl5BanBnXkFtZTgwOTAyNzU3MDI@._V1_SX300.jpg
Batman Begins	3befc231-f42c-47f3-9d39-b73d67aa35d3	2005	https://m.media-amazon.com/images/M/MV5BZmUwNGU2ZmItMmRiNC00MjhlLTg5YWUtODMyNzkxODYzMmZlXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_SX300.jpg
The Dark Knight	f2c7f7e5-f4f5-4a12-a304-78b8b3f27af3	2008	https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SY1000_CR0,0,675,1000_AL_.jpg​
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: developers
--

COPY public.person (person_id, person_name, birthday, birthplace) FROM stdin;
e3b5c997-9bd4-49a7-8741-6b7f588aea60	Christopher Nolan	1970-07-30	London, UK
aecd5d8b-36a2-49b0-ae71-4956b06e8312	Christian Bale	1974-01-30	Haverfordwest, UK
f6e3bc1e-1c42-4826-a5b6-54fc3c38af55	Bob Kane	1915-10-24	New York, USA
6f548380-db99-4e79-8f2a-445c752e6879	Michael Caine	1933-03-14	London, UK
66e9e53a-bbd8-4796-9010-46b8005fc335	David S. Goyer	1965-12-22	Michigan, USA
\.


--
-- Data for Name: profession; Type: TABLE DATA; Schema: public; Owner: developers
--

COPY public.profession (profession_id, profession_name) FROM stdin;
1	Director
2	Writer
3	Actor
\.


--
-- Data for Name: worked_on; Type: TABLE DATA; Schema: public; Owner: developers
--

COPY public.worked_on (person_id, profession_id, film_id, role) FROM stdin;
e3b5c997-9bd4-49a7-8741-6b7f588aea60	1	3befc231-f42c-47f3-9d39-b73d67aa35d3	Director
aecd5d8b-36a2-49b0-ae71-4956b06e8312	3	3befc231-f42c-47f3-9d39-b73d67aa35d3	Batman
f6e3bc1e-1c42-4826-a5b6-54fc3c38af55	2	3befc231-f42c-47f3-9d39-b73d67aa35d3	Writer
6f548380-db99-4e79-8f2a-445c752e6879	3	3befc231-f42c-47f3-9d39-b73d67aa35d3	Alfred
e3b5c997-9bd4-49a7-8741-6b7f588aea60	1	f2c7f7e5-f4f5-4a12-a304-78b8b3f27af3	Director
aecd5d8b-36a2-49b0-ae71-4956b06e8312	3	f2c7f7e5-f4f5-4a12-a304-78b8b3f27af3	Batman
6f548380-db99-4e79-8f2a-445c752e6879	3	f2c7f7e5-f4f5-4a12-a304-78b8b3f27af3	Alfred
66e9e53a-bbd8-4796-9010-46b8005fc335	2	3befc231-f42c-47f3-9d39-b73d67aa35d3	Writer
66e9e53a-bbd8-4796-9010-46b8005fc335	2	f2c7f7e5-f4f5-4a12-a304-78b8b3f27af3	Writer
f6e3bc1e-1c42-4826-a5b6-54fc3c38af55	2	f2c7f7e5-f4f5-4a12-a304-78b8b3f27af3	Writer
\.


--
-- Name: genre_filmed_genre_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developers
--

SELECT pg_catalog.setval('public.genre_filmed_genre_id_seq', 1, false);


--
-- Name: genre_genre_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developers
--

SELECT pg_catalog.setval('public.genre_genre_id_seq', 1, false);


--
-- Name: movies_release_year_seq; Type: SEQUENCE SET; Schema: public; Owner: developers
--

SELECT pg_catalog.setval('public.movies_release_year_seq', 13, true);


--
-- Name: proffesion_proffesion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developers
--

SELECT pg_catalog.setval('public.proffesion_proffesion_id_seq', 1, false);


--
-- Name: worked_on_proffesion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developers
--

SELECT pg_catalog.setval('public.worked_on_proffesion_id_seq', 1, true);


--
-- Name: genre_filmed genre_filmed_pkey; Type: CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.genre_filmed
    ADD CONSTRAINT genre_filmed_pkey PRIMARY KEY (genre_id, film_id);


--
-- Name: genre genre_pkey; Type: CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT genre_pkey PRIMARY KEY (genre_id);


--
-- Name: movies movies_pkey; Type: CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.movies
    ADD CONSTRAINT movies_pkey PRIMARY KEY (id);


--
-- Name: person person_pkey; Type: CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (person_id);


--
-- Name: profession proffesion_pkey; Type: CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.profession
    ADD CONSTRAINT proffesion_pkey PRIMARY KEY (profession_id);


--
-- Name: worked_on worked_on_pkey; Type: CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.worked_on
    ADD CONSTRAINT worked_on_pkey PRIMARY KEY (person_id, profession_id, film_id);


--
-- Name: genre_filmed genre_filmed_film_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.genre_filmed
    ADD CONSTRAINT genre_filmed_film_id_fkey FOREIGN KEY (film_id) REFERENCES public.movies(id);


--
-- Name: genre_filmed genre_filmed_genre_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.genre_filmed
    ADD CONSTRAINT genre_filmed_genre_id_fkey FOREIGN KEY (genre_id) REFERENCES public.genre(genre_id);


--
-- Name: worked_on worked_on_film_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.worked_on
    ADD CONSTRAINT worked_on_film_id_fkey FOREIGN KEY (film_id) REFERENCES public.movies(id);


--
-- Name: worked_on worked_on_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.worked_on
    ADD CONSTRAINT worked_on_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(person_id);


--
-- Name: worked_on worked_on_proffesion_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developers
--

ALTER TABLE ONLY public.worked_on
    ADD CONSTRAINT worked_on_proffesion_id_fkey FOREIGN KEY (profession_id) REFERENCES public.profession(profession_id);


--
-- PostgreSQL database dump complete
--

