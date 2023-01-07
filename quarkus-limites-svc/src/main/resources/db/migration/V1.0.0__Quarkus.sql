create table limitediario
(
    id bigint not null constraint limitediario_pkey primary key,
    agencia bigint,
    conta   bigint,
    data    date,
    valor   numeric(19, 2)
);

create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to limite;




