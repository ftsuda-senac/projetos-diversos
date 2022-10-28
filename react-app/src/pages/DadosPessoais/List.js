import React, { useState, useEffect } from 'react';
import { Link, createSearchParams, useSearchParams } from 'react-router-dom';
import Modal from "react-bootstrap/Modal";

// https://stackoverflow.com/a/30008115
function carregarDados(pagina = 0, qtde = 10) {
  return new Promise(function (resolve, reject) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/rest/pessoas?page=' + pagina + '&qtde=' + qtde, true);

    xhr.onload = function () {
      if (xhr.status == 200) {
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

function excluirPessoa(id) {
  return new Promise(function (resolve, reject) {
    if (id) {
      const xhr = new XMLHttpRequest();
      xhr.open('DELETE', 'http://localhost:8080/rest/pessoas/' + id, true);
      xhr.onload = function () {
        if (xhr.status >= 200 && xhr.status < 300) {
          resolve({
            status: xhr.status,
            msg: 'Pessoa com ID ' + id + ' excluida com sucesso'
          });
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
      };
      xhr.send();
    } else {
      reject({
        msg: 'ID não informado'
      });
    }
  });
}

export default function DadosPessoaisList() {

  const [queryParams, setQueryParams] = useSearchParams();

  const [msgSucesso, setMsgSucesso] = useState('');
  const [msgErro, setMsgErro] = useState('');

  const [showModalExcluir, setShowModalExcluir] = useState(false);
  const [modalExcluirInfo, setModalExcluirInfo] = useState({
    id: null,
    titulo: 'Confirmar exclusão',
    mensagem: 'Confirmar exclusão da pessoa ID?'
  });
  const [dadosLista, setDadosLista] = useState([]);
  const [dadosPagina, setDadosPagina] = useState({
    atual: 0,
    ultimaPagina: 0,
    qtde: 10,
    primeira: true,
    ultima: true
  });

  function carregarLista(pagina, qtde) {
    carregarDados(pagina, qtde).then(function (resposta) {
      setDadosLista(resposta.content);
      const dadosPag = {
        atual: resposta.number,
        ultimaPagina: resposta.totalPages - 1,
        qtde: resposta.size,
        primeira: resposta.first,
        ultima: resposta.last
      };
      setDadosPagina(dadosPag);
    });
  }

  // Executa lógica quando o searchParams for alterado
  useEffect(() => {
    carregarLista(queryParams.get('pagina') || dadosPagina.atual, queryParams.get('qtde') || dadosPagina.qtde)
  }, [queryParams]);

  function handlePaginacao(evt, proximaPagina, qtde = dadosPagina.qtde) {
    evt.preventDefault();
    if (proximaPagina >= 0 && proximaPagina <= dadosPagina.ultimaPagina) {
      setQueryParams(createSearchParams({
        pagina: proximaPagina,
        qtde: qtde
      }));
    }
  }

  function handleConfirmarExclusao(id) {
    setShowModalExcluir(true);
    setModalExcluirInfo({
      id: id,
      titulo: 'Confirmar exclusão',
      mensagem: 'Confirmar exclusão da pessoa ID ' + id + '?'
    });
  }

  function handleExclusao(id) {
    excluirPessoa(id).then(function(resp) {
      carregarLista(queryParams.get('pagina') || dadosPagina.atual, queryParams.get('qtde') || dadosPagina.qtde)
      alert(resp.msg);
    }).finally(function() {
      setShowModalExcluir(false);
    });
  }

  return (
    <>
      <div className="row">
        <div className="offset-lg-1 col-lg-10">
          <h1>Listagem de pessoas (VERSÃO React)</h1>
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
          <table className="table table-striped">
            <thead className="table-dark">
              <tr>
                <th scope="col">#</th>
                <th scope="col">Nome</th>
                <th scope="col">E-mail</th>
                <th scope="col">Data nascimento</th>
                <th scope="col">Status ativação</th>
                <th scope="col">Ações</th>
              </tr>
            </thead>
            <tbody>
              {
                dadosLista.map(function (dados, idx) {
                  return (
                    <tr key={`dados-${dados.id}`}>
                      <td>{dados.id}</td>
                      <td>{dados.nome}</td>
                      <td>{dados.email}</td>
                      <td>{new Date(dados.dataNascimento).toLocaleDateString('pt-BR')}</td>
                      <td>
                        {
                          (dados.cadastroAtivo === true) &&
                          <span className="badge bg-success">ATIVO</span>
                        }
                        {
                          (dados.cadastroAtivo !== true) &&
                          <span className="badge bg-danger">INATIVO</span>
                        }
                      </td>
                      <td>
                        <div className="btn-group" role="group" aria-label="Ações">
                          <Link to={`/pessoas/editar/${dados.id}`} className="btn btn-primary" role="button">
                            <i className="fas fa-edit"></i> Alterar
                          </Link>
                          <button type="button" className="btn btn-danger" data-bs-toggle="modal" data-bs-target="#modalExcluir" onClick={() => handleConfirmarExclusao(dados.id)}><i className="fas fa-trash-alt"></i> Excluir</button>
                        </div>
                      </td>
                    </tr>
                  );
                })
              }
            </tbody>
          </table>
          <nav aria-label="Paginação">
            <ul className="pagination justify-content-center">
              <li className={`page-item ${dadosPagina.primeira === true ? 'disabled' : ''}`} id="paginacaoPrimeiro">
                <a className="page-link" href="#" onClick={(evt) => handlePaginacao(evt, 0)}>Primeira</a>
              </li>
              {
                !((dadosPagina.atual - 1) < 0) &&
                <li className="page-item" id="paginacaoAnterior">
                  <a className="page-link" href="#" onClick={(evt) => handlePaginacao(evt, dadosPagina.atual - 1)}>{dadosPagina.atual}</a>
                </li>
              }
              <li className="page-item active" aria-current="page" id="paginacaoAtual">
                <span className="page-link">{dadosPagina.atual + 1}</span>
              </li>
              {
                !((dadosPagina.atual + 1) > dadosPagina.ultimaPagina) &&
                <li className="page-item" id="paginacaoProximo">
                  <a className="page-link" href="#" onClick={(evt) => handlePaginacao(evt, dadosPagina.atual + 1)}>{dadosPagina.atual + 2}</a>
                </li>
              }

              <li className={`page-item ${dadosPagina.ultima === true ? 'disabled' : ''}`} id="paginacaoUltimo">
                <a className="page-link" href="#" onClick={(evt) => handlePaginacao(evt, dadosPagina.ultimaPagina)}>Última</a>
              </li>
            </ul>
          </nav>
          <div>
            <Link to="/pessoas/novo" className="btn btn-lg btn-success" role="button">
              <i className="fas fa-plus-square"></i> Incluir novo
            </Link>
          </div>
        </div>
      </div>
      <Modal show={showModalExcluir}>
        <Modal.Header>{modalExcluirInfo.titulo}</Modal.Header>
        <Modal.Body>
          <p id="modalExcluirMsg">{modalExcluirInfo.mensagem}</p>
        </Modal.Body>
        <Modal.Footer>
          <button type="button" className="btn btn-outline-dark" onClick={() => setShowModalExcluir(false)}>Não</button>
          <button type="submit" className="btn btn-danger" onClick={() => handleExclusao(modalExcluirInfo.id)}>Sim</button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
