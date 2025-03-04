PGDMP      6                 }            TESTSWP391V4    17.2    17.2 L    -           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            .           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            /           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            0           1262    52029    TESTSWP391V4    DATABASE     �   CREATE DATABASE "TESTSWP391V4" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE "TESTSWP391V4";
                     postgres    false            �            1259    52031    tbl_cart    TABLE        CREATE TABLE public.tbl_cart (
    id bigint NOT NULL,
    quantity integer,
    product_id bigint,
    user_user_id bigint
);
    DROP TABLE public.tbl_cart;
       public         heap r       postgres    false            �            1259    52030    tbl_cart_id_seq    SEQUENCE     �   ALTER TABLE public.tbl_cart ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tbl_cart_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    218            �            1259    52037    tbl_category    TABLE     �   CREATE TABLE public.tbl_category (
    id bigint NOT NULL,
    image_name character varying(255),
    is_active boolean,
    name character varying(255)
);
     DROP TABLE public.tbl_category;
       public         heap r       postgres    false            �            1259    52036    tbl_category_id_seq    SEQUENCE     �   ALTER TABLE public.tbl_category ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tbl_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    220            �            1259    52045    tbl_feedback    TABLE     \  CREATE TABLE public.tbl_feedback (
    id bigint NOT NULL,
    comment character varying(1000) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    rating integer NOT NULL,
    replied boolean NOT NULL,
    staff_reply character varying(1000),
    updated_at timestamp(6) without time zone NOT NULL,
    user_id bigint NOT NULL
);
     DROP TABLE public.tbl_feedback;
       public         heap r       postgres    false            �            1259    52044    tbl_feedback_id_seq    SEQUENCE     �   ALTER TABLE public.tbl_feedback ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tbl_feedback_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    222            �            1259    52053    tbl_notification    TABLE     �   CREATE TABLE public.tbl_notification (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    message character varying(500) NOT NULL,
    read_status boolean NOT NULL,
    user_id bigint NOT NULL
);
 $   DROP TABLE public.tbl_notification;
       public         heap r       postgres    false            �            1259    52052    tbl_notification_id_seq    SEQUENCE     �   ALTER TABLE public.tbl_notification ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tbl_notification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    224            �            1259    52061    tbl_orderdetail    TABLE     �   CREATE TABLE public.tbl_orderdetail (
    id integer NOT NULL,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    mobile_no character varying(255),
    childid bigint
);
 #   DROP TABLE public.tbl_orderdetail;
       public         heap r       postgres    false            �            1259    52060    tbl_orderdetail_id_seq    SEQUENCE     �   ALTER TABLE public.tbl_orderdetail ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tbl_orderdetail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    226            �            1259    52068    tbl_permission    TABLE     �   CREATE TABLE public.tbl_permission (
    permission_name character varying(255) NOT NULL,
    permission_description character varying(255)
);
 "   DROP TABLE public.tbl_permission;
       public         heap r       postgres    false            �            1259    52076    tbl_post    TABLE     4  CREATE TABLE public.tbl_post (
    id bigint NOT NULL,
    content text NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    image_url character varying(255),
    title character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    author_id bigint NOT NULL
);
    DROP TABLE public.tbl_post;
       public         heap r       postgres    false            �            1259    52075    tbl_post_id_seq    SEQUENCE     �   ALTER TABLE public.tbl_post ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tbl_post_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    229            �            1259    52084    tbl_product    TABLE     �  CREATE TABLE public.tbl_product (
    id bigint NOT NULL,
    available boolean NOT NULL,
    created_at timestamp(6) without time zone,
    description character varying(5000),
    discount integer NOT NULL,
    discount_price double precision,
    image character varying(255),
    is_active boolean,
    manufacturer character varying(255) NOT NULL,
    price double precision,
    schedule character varying(1000) NOT NULL,
    side_effects character varying(500) NOT NULL,
    stock integer NOT NULL,
    target_group character varying(255) NOT NULL,
    title character varying(500),
    updated_at timestamp(6) without time zone,
    category_id bigint NOT NULL
);
    DROP TABLE public.tbl_product;
       public         heap r       postgres    false            �            1259    52083    tbl_product_id_seq    SEQUENCE     �   ALTER TABLE public.tbl_product ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tbl_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    231            �            1259    52092    tbl_productorder    TABLE     F  CREATE TABLE public.tbl_productorder (
    id bigint NOT NULL,
    order_date date,
    order_id character varying(255),
    payment_type character varying(255),
    price double precision,
    quantity integer,
    status character varying(255),
    order_detail_id integer,
    product_id bigint,
    user_user_id bigint
);
 $   DROP TABLE public.tbl_productorder;
       public         heap r       postgres    false            �            1259    52091    tbl_productorder_id_seq    SEQUENCE     �   ALTER TABLE public.tbl_productorder ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tbl_productorder_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    233            �            1259    52099 	   tbl_roles    TABLE     ~   CREATE TABLE public.tbl_roles (
    role_name character varying(255) NOT NULL,
    role_description character varying(255)
);
    DROP TABLE public.tbl_roles;
       public         heap r       postgres    false            �            1259    52106    tbl_roles_permissions    TABLE     �   CREATE TABLE public.tbl_roles_permissions (
    role_role_name character varying(255) NOT NULL,
    permissions_permission_name character varying(255) NOT NULL
);
 )   DROP TABLE public.tbl_roles_permissions;
       public         heap r       postgres    false            �            1259    52114 	   tbl_users    TABLE     Z  CREATE TABLE public.tbl_users (
    user_id bigint NOT NULL,
    account_non_locked boolean,
    birth_date timestamp(6) without time zone,
    email character varying(255),
    enabled boolean,
    fullname character varying(255),
    gender character varying(255),
    height double precision,
    parent_id bigint,
    password character varying(255),
    phone character varying(255),
    reset_token character varying(255),
    username character varying(255),
    verification_expiration timestamp(6) without time zone,
    verification_cod character varying(255),
    weight double precision
);
    DROP TABLE public.tbl_users;
       public         heap r       postgres    false            �            1259    52121    tbl_users_roles    TABLE        CREATE TABLE public.tbl_users_roles (
    user_user_id bigint NOT NULL,
    roles_role_name character varying(255) NOT NULL
);
 #   DROP TABLE public.tbl_users_roles;
       public         heap r       postgres    false            �            1259    52113    tbl_users_user_id_seq    SEQUENCE     �   ALTER TABLE public.tbl_users ALTER COLUMN user_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tbl_users_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    237                      0    52031    tbl_cart 
   TABLE DATA           J   COPY public.tbl_cart (id, quantity, product_id, user_user_id) FROM stdin;
    public               postgres    false    218   /g                 0    52037    tbl_category 
   TABLE DATA           G   COPY public.tbl_category (id, image_name, is_active, name) FROM stdin;
    public               postgres    false    220   gg                 0    52045    tbl_feedback 
   TABLE DATA           r   COPY public.tbl_feedback (id, comment, created_at, rating, replied, staff_reply, updated_at, user_id) FROM stdin;
    public               postgres    false    222   �g                 0    52053    tbl_notification 
   TABLE DATA           Y   COPY public.tbl_notification (id, created_at, message, read_status, user_id) FROM stdin;
    public               postgres    false    224   �h                 0    52061    tbl_orderdetail 
   TABLE DATA           _   COPY public.tbl_orderdetail (id, email, first_name, last_name, mobile_no, childid) FROM stdin;
    public               postgres    false    226   ;i                 0    52068    tbl_permission 
   TABLE DATA           Q   COPY public.tbl_permission (permission_name, permission_description) FROM stdin;
    public               postgres    false    227   ri       !          0    52076    tbl_post 
   TABLE DATA           d   COPY public.tbl_post (id, content, created_at, image_url, title, updated_at, author_id) FROM stdin;
    public               postgres    false    229   �i       #          0    52084    tbl_product 
   TABLE DATA           �   COPY public.tbl_product (id, available, created_at, description, discount, discount_price, image, is_active, manufacturer, price, schedule, side_effects, stock, target_group, title, updated_at, category_id) FROM stdin;
    public               postgres    false    231   �j       %          0    52092    tbl_productorder 
   TABLE DATA           �   COPY public.tbl_productorder (id, order_date, order_id, payment_type, price, quantity, status, order_detail_id, product_id, user_user_id) FROM stdin;
    public               postgres    false    233   �k       &          0    52099 	   tbl_roles 
   TABLE DATA           @   COPY public.tbl_roles (role_name, role_description) FROM stdin;
    public               postgres    false    234   Al       '          0    52106    tbl_roles_permissions 
   TABLE DATA           \   COPY public.tbl_roles_permissions (role_role_name, permissions_permission_name) FROM stdin;
    public               postgres    false    235   �l       )          0    52114 	   tbl_users 
   TABLE DATA           �   COPY public.tbl_users (user_id, account_non_locked, birth_date, email, enabled, fullname, gender, height, parent_id, password, phone, reset_token, username, verification_expiration, verification_cod, weight) FROM stdin;
    public               postgres    false    237   �l       *          0    52121    tbl_users_roles 
   TABLE DATA           H   COPY public.tbl_users_roles (user_user_id, roles_role_name) FROM stdin;
    public               postgres    false    238    n       1           0    0    tbl_cart_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.tbl_cart_id_seq', 1, false);
          public               postgres    false    217            2           0    0    tbl_category_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.tbl_category_id_seq', 1, false);
          public               postgres    false    219            3           0    0    tbl_feedback_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.tbl_feedback_id_seq', 1, false);
          public               postgres    false    221            4           0    0    tbl_notification_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.tbl_notification_id_seq', 1, false);
          public               postgres    false    223            5           0    0    tbl_orderdetail_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.tbl_orderdetail_id_seq', 1, true);
          public               postgres    false    225            6           0    0    tbl_post_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.tbl_post_id_seq', 2, true);
          public               postgres    false    228            7           0    0    tbl_product_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.tbl_product_id_seq', 3, true);
          public               postgres    false    230            8           0    0    tbl_productorder_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tbl_productorder_id_seq', 1, true);
          public               postgres    false    232            9           0    0    tbl_users_user_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.tbl_users_user_id_seq', 1, true);
          public               postgres    false    236            Z           2606    52035    tbl_cart tbl_cart_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.tbl_cart
    ADD CONSTRAINT tbl_cart_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.tbl_cart DROP CONSTRAINT tbl_cart_pkey;
       public                 postgres    false    218            \           2606    52043    tbl_category tbl_category_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.tbl_category
    ADD CONSTRAINT tbl_category_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.tbl_category DROP CONSTRAINT tbl_category_pkey;
       public                 postgres    false    220            ^           2606    52051    tbl_feedback tbl_feedback_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.tbl_feedback
    ADD CONSTRAINT tbl_feedback_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.tbl_feedback DROP CONSTRAINT tbl_feedback_pkey;
       public                 postgres    false    222            `           2606    52059 &   tbl_notification tbl_notification_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.tbl_notification
    ADD CONSTRAINT tbl_notification_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.tbl_notification DROP CONSTRAINT tbl_notification_pkey;
       public                 postgres    false    224            b           2606    52067 $   tbl_orderdetail tbl_orderdetail_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.tbl_orderdetail
    ADD CONSTRAINT tbl_orderdetail_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.tbl_orderdetail DROP CONSTRAINT tbl_orderdetail_pkey;
       public                 postgres    false    226            d           2606    52074 "   tbl_permission tbl_permission_pkey 
   CONSTRAINT     m   ALTER TABLE ONLY public.tbl_permission
    ADD CONSTRAINT tbl_permission_pkey PRIMARY KEY (permission_name);
 L   ALTER TABLE ONLY public.tbl_permission DROP CONSTRAINT tbl_permission_pkey;
       public                 postgres    false    227            f           2606    52082    tbl_post tbl_post_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.tbl_post
    ADD CONSTRAINT tbl_post_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.tbl_post DROP CONSTRAINT tbl_post_pkey;
       public                 postgres    false    229            h           2606    52090    tbl_product tbl_product_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.tbl_product
    ADD CONSTRAINT tbl_product_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.tbl_product DROP CONSTRAINT tbl_product_pkey;
       public                 postgres    false    231            l           2606    52098 &   tbl_productorder tbl_productorder_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.tbl_productorder
    ADD CONSTRAINT tbl_productorder_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.tbl_productorder DROP CONSTRAINT tbl_productorder_pkey;
       public                 postgres    false    233            r           2606    52112 0   tbl_roles_permissions tbl_roles_permissions_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.tbl_roles_permissions
    ADD CONSTRAINT tbl_roles_permissions_pkey PRIMARY KEY (role_role_name, permissions_permission_name);
 Z   ALTER TABLE ONLY public.tbl_roles_permissions DROP CONSTRAINT tbl_roles_permissions_pkey;
       public                 postgres    false    235    235            p           2606    52105    tbl_roles tbl_roles_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.tbl_roles
    ADD CONSTRAINT tbl_roles_pkey PRIMARY KEY (role_name);
 B   ALTER TABLE ONLY public.tbl_roles DROP CONSTRAINT tbl_roles_pkey;
       public                 postgres    false    234            t           2606    52120    tbl_users tbl_users_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.tbl_users
    ADD CONSTRAINT tbl_users_pkey PRIMARY KEY (user_id);
 B   ALTER TABLE ONLY public.tbl_users DROP CONSTRAINT tbl_users_pkey;
       public                 postgres    false    237            v           2606    52125 $   tbl_users_roles tbl_users_roles_pkey 
   CONSTRAINT     }   ALTER TABLE ONLY public.tbl_users_roles
    ADD CONSTRAINT tbl_users_roles_pkey PRIMARY KEY (user_user_id, roles_role_name);
 N   ALTER TABLE ONLY public.tbl_users_roles DROP CONSTRAINT tbl_users_roles_pkey;
       public                 postgres    false    238    238            j           2606    52127 '   tbl_product ukcou7p71iu1bfkbxq7adatkhm7 
   CONSTRAINT     c   ALTER TABLE ONLY public.tbl_product
    ADD CONSTRAINT ukcou7p71iu1bfkbxq7adatkhm7 UNIQUE (title);
 Q   ALTER TABLE ONLY public.tbl_product DROP CONSTRAINT ukcou7p71iu1bfkbxq7adatkhm7;
       public                 postgres    false    231            n           2606    52129 ,   tbl_productorder ukfa4e97ew1icp3w5g4kq5y1pbr 
   CONSTRAINT     r   ALTER TABLE ONLY public.tbl_productorder
    ADD CONSTRAINT ukfa4e97ew1icp3w5g4kq5y1pbr UNIQUE (order_detail_id);
 V   ALTER TABLE ONLY public.tbl_productorder DROP CONSTRAINT ukfa4e97ew1icp3w5g4kq5y1pbr;
       public                 postgres    false    233            �           2606    52180 1   tbl_roles_permissions fk39se7p5bdvew7n2xy6ndoucqo    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_roles_permissions
    ADD CONSTRAINT fk39se7p5bdvew7n2xy6ndoucqo FOREIGN KEY (role_role_name) REFERENCES public.tbl_roles(role_name);
 [   ALTER TABLE ONLY public.tbl_roles_permissions DROP CONSTRAINT fk39se7p5bdvew7n2xy6ndoucqo;
       public               postgres    false    4720    234    235            z           2606    52145 ,   tbl_notification fk4fm57v2m7c6xtsyrdf8htnxdt    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_notification
    ADD CONSTRAINT fk4fm57v2m7c6xtsyrdf8htnxdt FOREIGN KEY (user_id) REFERENCES public.tbl_users(user_id);
 V   ALTER TABLE ONLY public.tbl_notification DROP CONSTRAINT fk4fm57v2m7c6xtsyrdf8htnxdt;
       public               postgres    false    4724    224    237            w           2606    52130 $   tbl_cart fk79js0lkim8x3pcso32411e56o    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_cart
    ADD CONSTRAINT fk79js0lkim8x3pcso32411e56o FOREIGN KEY (product_id) REFERENCES public.tbl_product(id);
 N   ALTER TABLE ONLY public.tbl_cart DROP CONSTRAINT fk79js0lkim8x3pcso32411e56o;
       public               postgres    false    231    218    4712            �           2606    52190 +   tbl_users_roles fkcvc82fmgkf6aplcv4tni833kw    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_users_roles
    ADD CONSTRAINT fkcvc82fmgkf6aplcv4tni833kw FOREIGN KEY (user_user_id) REFERENCES public.tbl_users(user_id);
 U   ALTER TABLE ONLY public.tbl_users_roles DROP CONSTRAINT fkcvc82fmgkf6aplcv4tni833kw;
       public               postgres    false    4724    237    238            x           2606    52135 $   tbl_cart fkdssj8e7hwp3doos3xbmhedrpo    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_cart
    ADD CONSTRAINT fkdssj8e7hwp3doos3xbmhedrpo FOREIGN KEY (user_user_id) REFERENCES public.tbl_users(user_id);
 N   ALTER TABLE ONLY public.tbl_cart DROP CONSTRAINT fkdssj8e7hwp3doos3xbmhedrpo;
       public               postgres    false    218    4724    237            y           2606    52140 (   tbl_feedback fkdwniwjdihpjhr1hpd1356x1k9    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_feedback
    ADD CONSTRAINT fkdwniwjdihpjhr1hpd1356x1k9 FOREIGN KEY (user_id) REFERENCES public.tbl_users(user_id);
 R   ALTER TABLE ONLY public.tbl_feedback DROP CONSTRAINT fkdwniwjdihpjhr1hpd1356x1k9;
       public               postgres    false    4724    222    237            |           2606    52155 '   tbl_product fkfq7110lh85cseoy13cgni7pet    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_product
    ADD CONSTRAINT fkfq7110lh85cseoy13cgni7pet FOREIGN KEY (category_id) REFERENCES public.tbl_category(id);
 Q   ALTER TABLE ONLY public.tbl_product DROP CONSTRAINT fkfq7110lh85cseoy13cgni7pet;
       public               postgres    false    220    4700    231            }           2606    52165 ,   tbl_productorder fkhconfib4qwdm92e25gg96nw1x    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_productorder
    ADD CONSTRAINT fkhconfib4qwdm92e25gg96nw1x FOREIGN KEY (product_id) REFERENCES public.tbl_product(id);
 V   ALTER TABLE ONLY public.tbl_productorder DROP CONSTRAINT fkhconfib4qwdm92e25gg96nw1x;
       public               postgres    false    231    233    4712            �           2606    52175 1   tbl_roles_permissions fkjajpgvwkdr5qc3okyf6m68cid    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_roles_permissions
    ADD CONSTRAINT fkjajpgvwkdr5qc3okyf6m68cid FOREIGN KEY (permissions_permission_name) REFERENCES public.tbl_permission(permission_name);
 [   ALTER TABLE ONLY public.tbl_roles_permissions DROP CONSTRAINT fkjajpgvwkdr5qc3okyf6m68cid;
       public               postgres    false    4708    235    227            ~           2606    52170 ,   tbl_productorder fkjoqtxtlxotl7yykj994jmqof7    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_productorder
    ADD CONSTRAINT fkjoqtxtlxotl7yykj994jmqof7 FOREIGN KEY (user_user_id) REFERENCES public.tbl_users(user_id);
 V   ALTER TABLE ONLY public.tbl_productorder DROP CONSTRAINT fkjoqtxtlxotl7yykj994jmqof7;
       public               postgres    false    4724    233    237                       2606    52160 ,   tbl_productorder fko210ur1dvy38ctwtsgnl5jcpi    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_productorder
    ADD CONSTRAINT fko210ur1dvy38ctwtsgnl5jcpi FOREIGN KEY (order_detail_id) REFERENCES public.tbl_orderdetail(id);
 V   ALTER TABLE ONLY public.tbl_productorder DROP CONSTRAINT fko210ur1dvy38ctwtsgnl5jcpi;
       public               postgres    false    226    233    4706            �           2606    52185 +   tbl_users_roles fkovijofsyuytw8xll2andf8e4b    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_users_roles
    ADD CONSTRAINT fkovijofsyuytw8xll2andf8e4b FOREIGN KEY (roles_role_name) REFERENCES public.tbl_roles(role_name);
 U   ALTER TABLE ONLY public.tbl_users_roles DROP CONSTRAINT fkovijofsyuytw8xll2andf8e4b;
       public               postgres    false    4720    238    234            {           2606    52150 $   tbl_post fkp9u6uhc8o4y0rdusrdv4elrpn    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_post
    ADD CONSTRAINT fkp9u6uhc8o4y0rdusrdv4elrpn FOREIGN KEY (author_id) REFERENCES public.tbl_users(user_id);
 N   ALTER TABLE ONLY public.tbl_post DROP CONSTRAINT fkp9u6uhc8o4y0rdusrdv4elrpn;
       public               postgres    false    237    4724    229               (   x�35�4�45 ".SCNCNS�25�4��F\1z\\\ ibD         W   x�3���,�t�8�!Q� ��<�����fr�p�%&'g��'�e�U�A�
!��
�\��pI�T8q��U$cS������ �+�         �   x�}�1n�0Cg����%!@.Pt�ҎY~��؈�Hr
߾���|$�Q sz��;�8x&�9�U6�oj4�?{2���u�wֻ����J��P����c����P!���|��N���������凑׉��̡���@�6�w��e�/I��"X��B��3֚� [�ca�P$��Z���Z�?��^I         �   x�}̻�0 �ڞ�2 �'$�HA)�����?�X?	M:x%�Rc'M'-h3�~0V�5h{�H��,T`u&�u�9�"؋�䨮�H�ǯ��v7�>����-Pl�+�R)�8K�Q_�w:����榍n?��Sp�?Ѭ88         '   x�3�L)N!NǼ�Ĕ��<NC#c�(g�W� �$
�         I   x��t��w	u	�tN�S(�L-W((�O)M.)�ru�A�OM�,AȻ�������HI�I-IE������ &!�      !   �   x�}��
�0D��W�hIӆ�K���b���&�&�ſ���d;�gF0��;tأ��`�+6�(��M*e��3."�F,�Γ|�f��R��D��՗�3���ŋ��V�!�"	9�<���c���$���	5L�?���,�ai�0AB'{T�i�iԿ�P�������xy��x)9��ۃ2��:8Epz�)�O�r%      #   �   x����j1���)�.�d�����Rz(BI7��IIV}���l{���g~>B#hԴ@��Vj�V˵ij�Kx�}��N1�/�2(�'�۽���w8�����p*%��.fΰ�g'���J�9�w��	���R������y�@��)>'�.:N��5U����M0%����b(A�1��
2S�(��	�D���;�-(B0�ol��W����0�V��F���n�      %   �   x�m�;�0@g��@��Ą=̴��%"D*���tB�����gF�h�Eע�n8�u�!V�eib�u����d��	ô��O�>M:B �����J��R���+��u%����N�D�!�w��Z�њ�j��r���C���̸3�| M8�      &   _   x�s��u�-N-R(��I�rt����tL��̃����#�f�%��A$B����R�Ks�@�@��C��8�K��|Ss���1z\\\ �0#<      '   =   x���q�wt�����t��w	u	�
BH��x�`�pq�qqE�
vB3+F��� K�N      )   �   x�e�_O�0ş�O�����G�qq� Cp�f/�UǴ��.��v�3�����r.�D� ��x4alB1�!��M���
4n)U�J��fE��|�Ň�4��7ˏ�,�i�v#���~���5}I��Y�2Z&t�F�Сx.�'l��>�R*��Z칬��hi��[׏������un��gLr�p%��6E�j}����i?}FFG�^ѣF��S#���uU�/�x�<�Z�s�      *   *   x�3�tt����25���q���!��`� .S#$N� R[     