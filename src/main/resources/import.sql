insert into accounts(id, name, surname, pesel) values (1, 'Jan', 'Kowalski', '11223344556');

insert into subaccounts(account_id, `value`, currency) values (1, 1000, 'PLN');
insert into subaccounts values (1, 0, 'USD');