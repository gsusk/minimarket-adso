create table minimarket.category
(
    id   bigint       not null
        primary key auto_increment,
    name varchar(255) not null,
    constraint uk_category_name
        unique (name)
);

create table minimarket.product
(
    price       decimal(19, 2) not null,
    stock       int            not null,
    category_id bigint         not null,
    created_at  datetime(6)    null,
    id          bigint         not null
        primary key auto_increment,
    updated_at  datetime(6)    null,
    description varchar(255)   null,
    name        varchar(255)   not null,
    constraint fk_product_category
        foreign key (category_id) references minimarket.category (id),
    check (`stock` >= 0)
);

create table minimarket.image
(
    created_at datetime(6)  null,
    id         bigint       not null
        primary key auto_increment,
    product_id bigint       not null,
    url        varchar(255) not null,
    constraint fk_product_image_product
        foreign key (product_id) references minimarket.product (id)
);

create table minimarket.user
(
    created_at   datetime(6)            null,
    id           bigint                 not null
        primary key auto_increment,
    updated_at   datetime(6)            null,
    address      varchar(255)           null,
    email        varchar(255)           not null,
    last_name    varchar(255)           not null,
    name         varchar(255)           not null,
    password     varchar(255)           not null,
    phone_number varchar(255)           null,
    status       varchar(255)           null,
    role         enum ('ADMIN', 'USER') not null,
    constraint uk_user_email
        unique (email)
);

create table minimarket.cart
(
    created_at datetime(6)                                        null,
    id         bigint                                             not null
        primary key auto_increment,
    updated_at datetime(6)                                        null,
    user_id    bigint                                             null,
    guest_id   binary(16)                                         null,
    status     enum ('ABANDONED', 'ACTIVE', 'COMPLETE', 'MERGED') not null,
    constraint fk_cart_user
        foreign key (user_id) references minimarket.user (id),
    check (((`user_id` is null) and (`guest_id` is not null)) or ((`user_id` is not null) and (`guest_id` is null)))
);

create table minimarket.cart_item
(
    quantity   int            not null,
    unit_price decimal(19, 4) not null,
    cart_id    bigint         not null,
    created_at datetime(6)    null,
    product_id bigint         not null,
    updated_at datetime(6)    null,
    primary key (product_id, cart_id),
    constraint fk_cart_item_cart
        foreign key (cart_id) references minimarket.cart (id),
    constraint fk_cart_item_product
        foreign key (product_id) references minimarket.product (id)
);

create table minimarket.orders
(
    total_amount decimal(19, 2)                                       null,
    created_at   datetime(6)                                          null,
    updated_at   datetime(6)                                          null,
    user_id      bigint                                               null,
    id           binary(16)                                           not null
        primary key,
    email        varchar(255)                                         not null,
    status       enum ('CANCELLED', 'COMPLETED', 'FAILED', 'PENDING') not null,
    constraint fk_order_user
        foreign key (user_id) references minimarket.user (id)
);

create table minimarket.order_item
(
    price      decimal(19, 2) not null,
    quantity   int            not null,
    sub_total  decimal(19, 2) not null,
    created_at datetime(6)    null,
    product_id bigint         not null,
    updated_at datetime(6)    null,
    id         binary(16)     not null
        primary key,
    order_id   binary(16)     not null,
    constraint fk_order_item_order
        foreign key (order_id) references minimarket.orders (id),
    constraint fk_order_item_product
        foreign key (product_id) references minimarket.product (id),
    check (`quantity` >= 1)
);