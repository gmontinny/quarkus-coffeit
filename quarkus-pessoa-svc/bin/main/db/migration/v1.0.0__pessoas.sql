create table pessoas
(
    id bigint not null constraint pessoas_pkey primary key,
    nome text(200),
    cpf_cpnj text(14),
    agencia bigint,
    conta   bigint
);

create sequence pessoas_sequence;

alter sequence pessoas_sequence owner to limite;
