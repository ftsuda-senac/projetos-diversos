INSERT INTO interesse (id, nome) VALUES
(NEXT VALUE FOR seq_interesse_id, 'Java'),
(NEXT VALUE FOR seq_interesse_id, 'Python'),
(NEXT VALUE FOR seq_interesse_id, 'Javascript'),
(NEXT VALUE FOR seq_interesse_id, 'User Experience'),
(NEXT VALUE FOR seq_interesse_id, 'Banco de dados'),
(NEXT VALUE FOR seq_interesse_id, 'Testes');

INSERT INTO pessoa (id, nome, apelido, descricao, data_nascimento, 
email, telefone, senha, numero, altura, peso, genero, 
cadastro_ativo, data_cadastro) VALUES
(NEXT VALUE FOR seq_pessoa_id, 'Agenor Barros', 'abarros', 'Descrição de Agenor Barros', '1981-11-21',
'abarros@teste.com.br', '(11) 99876-1029', 'abcd1234', 86, 1.75, 89.4, 1,
true, CURRENT_TIMESTAMP()),
(NEXT VALUE FOR seq_pessoa_id, 'Victória Almeida', 'valmeida', 'Descrição de Victória Almeida', '1961-04-04',
'valmeida@teste.com.br', '(21) 91234-0088', 'abcd1234', 21, 1.67, 79.0, 0,
true, CURRENT_TIMESTAMP()),
(NEXT VALUE FOR seq_pessoa_id, 'Amanda Araujo', 'aaraujo', 'Descrição de Amanda Araujo','1978-12-07',
'aaraujo@teste.com.br', '(61) 97612-3490', 'abcd1234', 75, 1.70, 75.0, 0,
true, CURRENT_TIMESTAMP()),
(NEXT VALUE FOR seq_pessoa_id, 'Gustavo Marques', 'gmarques', 'Descrição de Gustavo Marques', '1985-10-26',
'gmarques@teste.com.br', '(11) 96654-1928', 'abcd1234', 8, 1.89, 91.0, 1,
true, CURRENT_TIMESTAMP()),
(NEXT VALUE FOR seq_pessoa_id, 'Beatriz Marinho', 'bmarinho', 'Descrição de Beatriz Marinho', '1990-01-29',
'bmarinho@teste.com.br', '(15) 91467-0912', 'abcd1234', 21, 1.63, 75.5, 0,
true, CURRENT_TIMESTAMP()),
(NEXT VALUE FOR seq_pessoa_id, 'Enzo Cardoso', 'ecardoso', 'Descrição de Enzo Cardoso', '1982-10-27',
'ecardoso@teste.com.br', '(21) 99901-9898', 'abcd1234', 34, 1.78, 90.0, 1,
true, CURRENT_TIMESTAMP());

INSERT INTO foto_pessoa (id, nome_arquivo, legenda, pessoa_id) VALUES
(NEXT VALUE FOR seq_fotopessoa_id, 'avatar-m1.jpg', 'Foto de Agenor Barros', 1),
(NEXT VALUE FOR seq_fotopessoa_id, 'avatar-f1.jpg', 'Foto de Victória Almeida', 2),
(NEXT VALUE FOR seq_fotopessoa_id, 'avatar-f2.jpg', 'Foto de Amanda Araujo', 3),
(NEXT VALUE FOR seq_fotopessoa_id, 'avatar-m2.jpg', 'Foto de Gustavo Marques', 4),
(NEXT VALUE FOR seq_fotopessoa_id, 'avatar-f3.jpg', 'Foto de Beatriz Marinho', 5),
(NEXT VALUE FOR seq_fotopessoa_id, 'avatar-m3.jpg', 'Foto de Enzo Cardoso', 6);

INSERT INTO pessoa_interesse(pessoa_id, interesse_id) VALUES
(1, 1),
(1, 3),
(1, 5),
(2, 2),
(2, 4),
(3, 1),
(3, 2),
(3, 3),
(4, 3),
(4, 4),
(5, 1),
(5, 5);
