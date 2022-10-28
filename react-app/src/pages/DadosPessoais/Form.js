import React, { useState, useEffect } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom'

function carregarInteresses() {
  return new Promise(function (resolve, reject) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/rest/interesses', true);
    xhr.onload = function () {
      if (xhr.status === 200) {
        resolve(JSON.parse(xhr.responseText));
      } else {
        reject({
          status: xhr.status,
          msg: 'Erro ao carregar dados'
        });
      }
    };
    xhr.onerror = function () {
      reject({
        status: xhr.status,
        msg: 'Erro ao carregar dados'
      })
    }
    xhr.send();
  });
}
function carregarDados(id) {
  return new Promise(function (resolve, reject) {
    if (id) {
      const xhr = new XMLHttpRequest();
      xhr.open('GET', 'http://localhost:8080/rest/pessoas/' + id, true);

      xhr.onload = function () {
        if (xhr.status === 200) {
          resolve(JSON.parse(xhr.responseText));
        } else {
          reject({
            status: xhr.status,
            msg: 'Erro ao carregar dados'
          });
        }
      };
      xhr.onerror = function () {
        reject({
          status: xhr.status,
          msg: 'Erro ao carregar dados'
        })
      }
      xhr.send();
    } else {
      reject({
        msg: 'ID não informado'
      });
    }
  });
}

function salvarDados(dados) {
  return new Promise(function (resolve, reject) {
    const xhr = new XMLHttpRequest();

    if (dados.id) {
      // Alterar com PUT
      xhr.open('PUT', 'http://localhost:8080/rest/pessoas/' + dados.id, true);
    } else {
      // Incluir com POST
      xhr.open('POST', 'http://localhost:8080/rest/pessoas', true);
    }

    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.setRequestHeader('Accept', 'application/json');
    xhr.onload = function () {
      if (xhr.status >= 200 && xhr.status < 300) {
        let msg = 'Pessoa incluida com sucesso';
        if (dados.id) {
          msg = 'Pessoa com ID ' + dados.id + ' alterada com sucesso'
        }
        resolve({
          status: xhr.status,
          msg: msg
        });
      } else {
        reject({
          status: xhr.status,
          msg: 'Erro ao carregar dados'
        });
      }
    }
    xhr.send(JSON.stringify(dados));
  });
}

export default function DadosPessoaisForm() {

  const navigate = useNavigate();

  const urlParams = useParams();
  const idPessoa = urlParams.id;

  const [msgSucesso, setMsgSucesso] = useState('');
  const [msgErro, setMsgErro] = useState('');
  const [dados, setDados] = useState(null)
  const [interesses, setInteresses] = useState([])

  useEffect(() => {
    carregarInteresses().then(function (resposta) {
      setInteresses(resposta);
    });
  }, []);

  useEffect(() => {
    if (idPessoa) {
      carregarDados(idPessoa).then(function (resposta) {
        setDados(resposta);
      });
    }
  }, [idPessoa])

  function handleSalvar(evt) {
    evt.preventDefault();

    // Percorre os checkboxes dos interesses e pega os valores selecionados
    const interesses = document.querySelectorAll('[name="interessesIds"]')
    const interessesIdsArr = []
    for (const elCheckbox of interesses) {
      if (elCheckbox.checked) {
        interessesIdsArr.push(+elCheckbox.value)
      }
    }

    const form = evt.target;
    const dadosParaSalvar = {
      id: idPessoa,
      nome: form.nome.value,
      apelido: form.apelido.value,
      descricao: form.descricao.value,
      dataNascimento: form.dataNascimento.value,
      email: form.email.value,
      telefone: form.telefone.value,
      senha: form.senha?.value, // O "?" indica que senha é opcional - se não existir, não dá erro
      senhaRepetida: form.senhaRepetida?.value,
      numero: +form.numero.value, // O "+" converte para número
      altura: +form.altura.value,
      peso: +form.peso.value,
      genero: +form.genero.value,
      interessesIds: interessesIdsArr
    };
    console.log('JSON montado: ' + JSON.stringify(dadosParaSalvar));
    salvarDados(dadosParaSalvar).then(function(resposta) {
      alert(resposta.msg);
      navigate('/pessoas');
    });
  }

  return (
    <div className="row">
      <div className="offset-lg-1 col-lg-10">
        <h1>{idPessoa ? `Alteração de pessoa - ID ${idPessoa}` : 'Inclusão de pessoa'} (VERSÃO React)</h1>
        {
          msgSucesso.length > 0 &&
          <div className="alert alert-success" role="alert">
            <h4 className="alert-heading">Sucesso</h4>
            <p className="mb-0">{msgSucesso}</p>
          </div>
        }
        {
          msgErro.length > 0 &&
          <div className="alert alert-danger" role="alert">
            <h4 className="alert-heading">Erro</h4>
            <p className="mb-0">{msgErro}</p>
          </div>
        }

        <form method="post" id="dadosForm" noValidate={true} onSubmit={(evt) => handleSalvar(evt)}>
          <div className="row mb-3">
            <label htmlFor="txtNome" className="col-sm-2 col-form-label">Nome</label>
            <div className="col-sm-10">
              <input
                type="text"
                name="nome"
                id="txtNome"
                className="form-control"
                placeholder="Preencha o nome completo"
                maxLength={100}
                required={true}
                defaultValue={dados?.nome}
              />
            </div>
          </div>
          <div className="row mb-3">
            <label htmlFor="txtApelido" className="col-sm-2 col-form-label">Apelido</label>
            <div className="col-sm-10">
              <input
                type="text"
                name="apelido"
                id="txtApelido"
                className="form-control"
                placeholder="Preencha um apelido"
                maxLength={100}
                required={true}
                defaultValue={dados?.apelido}
              />
            </div>
          </div>
          <div className="row mb-3">
            <label htmlFor="txtDescricao" className="col-sm-2 col-form-label">Descrição</label>
            <div className="col-sm-10">
              <textarea
                name="descricao"
                id="txtDescricao"
                className="form-control"
                placeholder="Descrição"
                maxLength={1000}
                defaultValue={dados?.descricao}
              />
            </div>
          </div>
          <div className="row mb-3">
            <label htmlFor="txtDataNascimento" className="col-sm-2 col-form-label">Data de nascimento</label>
            <div className="col-sm-10">
              <input
                type="date"
                name="dataNascimento"
                id="txtDataNascimento"
                className="form-control"
                required={true}
                defaultValue={dados?.dataNascimento}
              />
            </div>
          </div>
          <div className="row mb-3">
            <label htmlFor="txtEmail" className="col-sm-2 col-form-label">E-mail</label>
            <div className="col-sm-10">
              <input
                type="email"
                name="email"
                id="txtEmail"
                className="form-control"
                placeholder="E-mail válido"
                maxLength={100}
                required={true}
                defaultValue={dados?.email}
              />
            </div>
          </div>
          <div className="row mb-3">
            <label htmlFor="txtTelefone" className="col-sm-2 col-form-label">Telefone</label>
            <div className="col-sm-10">
              <input
                type="text"
                name="telefone"
                id="txtTelefone"
                className="form-control"
                placeholder="Formato (99) 99999-9999"
                maxLength={20}
                defaultValue={dados?.telefone}
              />
            </div>
          </div>
          {
            // só preenche senha na inclusao - não pode alterar senha
            !idPessoa &&
            <>
              <div className="row mb-3" id="campoSenha">
                <label htmlFor="txtSenha" className="col-sm-2 col-form-label">Senha</label>
                <div className="col-sm-10">
                  <input
                    type="password"
                    name="senha"
                    id="txtSenha"
                    className="form-control"
                    placeholder="Senha"
                  />
                </div>
              </div>
              <div className="row mb-3" id="campoSenhaRepetida">
                <label htmlFor="txtSenhaRepetida" className="col-sm-2 col-form-label">Repetir senha</label>
                <div className="col-sm-10">
                  <input
                    type="password"
                    name="senhaRepetida"
                    id="txtSenhaRepetida"
                    className="form-control"
                    placeholder="Repetir senha"
                  />
                </div>
              </div>
            </>
          }
          <div className="row mb-3">
            <label htmlFor="txtNumero" className="col-sm-2 col-form-label">Número de 1 a 99</label>
            <div className="col-sm-10">
              <input
                type="number"
                name="numero"
                id="txtNumero"
                className="form-control"
                placeholder="Número de 1 a 99"
                min="1"
                max="99"
                defaultValue={dados?.numero}
              />
            </div>
          </div>
          <div className="row mb-3">
            <label htmlFor="txtAltura" className="col-sm-2 col-form-label">Altura</label>
            <div className="col-sm-10">
              <input
                type="number"
                name="altura"
                id="txtAltura"
                className="form-control"
                placeholder="Altura em metros com no máximo 2 casa decimais"
                min="0"
                max="3"
                step="0.01"
                defaultValue={dados?.altura}
              />
            </div>
          </div>
          <div className="row mb-3">
            <label htmlFor="txtPeso" className="col-sm-2 col-form-label">Peso</label>
            <div className="col-sm-10">
              <input
                type="number"
                name="peso"
                id="txtPeso"
                className="form-control"
                placeholder="Peso em Kg com no máximo 1 casa decimal"
                min="0"
                max="500"
                step="0.1"
                defaultValue={dados?.peso}
              />
            </div>
          </div>
          <fieldset className="row mb-3">
            <legend className="col-sm-2 col-form-label pt-0">Gênero</legend>
            <div className="col-sm-10">
              <div className="form-check form-check-inline">
                <input
                  type="radio"
                  name="genero"
                  id="generoI"
                  value="-1"
                  className="form-check-input"
                  defaultChecked={(dados?.genero !== '0' && dados?.genero !== '1' &&  dados?.genero !== '2')}
                />
                <label className="form-check-label" htmlFor="generoI">Não informar</label>
              </div>
              <div className="form-check form-check-inline">
                <input
                  type="radio"
                  name="genero"
                  id="generoF"
                  value="0"
                  className="form-check-input"
                  defaultChecked={(dados?.genero === '0')}
                />
                <label className="form-check-label" htmlFor="generoF">Feminino</label>
              </div>
              <div className="form-check form-check-inline">
                <input
                  type="radio"
                  name="genero"
                  id="generoM"
                  value="1"
                  className="form-check-input"
                  defaultChecked={(dados?.genero === '1')}
                />
                <label className="form-check-label" htmlFor="generoM">Masculino</label>
              </div>
              <div className="form-check form-check-inline">
                <input
                  type="radio"
                  name="genero"
                  id="generoO"
                  value="2"
                  className="form-check-input"
                  defaultChecked={(dados?.genero === '2')}
                />
                <label className="form-check-label" htmlFor="generoO">Outro</label>
              </div>
            </div>
          </fieldset>
          <fieldset className="row mb-3">
            <legend className="col-sm-2 col-form-label pt-0">Interesses</legend>
            <div className="col-sm-10" id="opcoesInteresses">
              {
                interesses.map(function (interesse, idx) {
                  const elementId = 'interesse' + interesse.id;
                  return (
                    <div key={elementId} className="form-check form-check-inline">
                      <input
                        type="checkbox"
                        name="interessesIds"
                        id={elementId}
                        value={interesse.id}
                        className="form-check-input"
                        defaultChecked={dados?.interessesIds.includes(interesse.id)}
                      />
                      <label className="form-check-label"
                        htmlFor={elementId}>{interesse.nome}</label>
                    </div>
                  );
                })
              }
            </div>
          </fieldset>

          <div className="row">
            <div className="col-md-3 offset-md-3">
              <div className="d-grid">
                <Link to="/pessoas" className="btn btn-outline-dark">Cancelar</Link>
              </div>
            </div>
            <div className="col-md-3">
              <div className="d-grid">
                <button type="submit" className="btn btn-success">Salvar</button>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}