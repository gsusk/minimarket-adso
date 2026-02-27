create table minimarket.payment
(
    amount            decimal(19, 2)                        not null,
    created_at        datetime(6)                           null,
    updated_at        datetime(6)                           null,
    id                binary(16)                            not null primary key,
    order_id          binary(16)                            not null,
    currency          varchar(10)                           not null default 'usd',
    payment_reference varchar(64)                           not null,
    status            enum ('PENDING', 'COMPLETED', 'FAILED') not null,
    constraint uk_payment_reference unique (payment_reference),
    constraint fk_payment_order foreign key (order_id) references minimarket.orders (id)
);
