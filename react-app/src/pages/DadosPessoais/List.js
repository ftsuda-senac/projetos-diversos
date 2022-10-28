import React, { useState } from 'react';

export default function DadosPessoaisList() {

  const [msgSucesso, setMsgSucesso] = useState('');
  const [msgErro, setMsgErro] = useState('');
  const [dadosLista, setDadosLista] = useState([]);

  return (
    <>
      <div className="row">
        <div className="offset-md-2 col-md-8">
          <h1>Listagem de pessoas</h1>
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
                          <a className="btn btn-primary" role="button"><i className="fas fa-edit"></i> Alterar</a>
                          <button type="button" className="btn btn-danger" data-bs-toggle="modal" data-bs-target="#modalExcluir"><i className="fas fa-trash-alt"></i> Excluir</button>
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
              <li className="page-item" id="paginacaoPrimeiro">
                <a className="page-link" href="#">Primeiro</a>
              </li>
              <li className="page-item" id="paginacaoAnterior">
                <a className="page-link" href="#">1</a>
              </li>
              <li className="page-item active" id="paginacaoAtual">
                <a className="page-link active" href="#">
                  <span>2</span><span className="sr-only">Página atual</span></a>
              </li>
              <li className="page-item" id="paginacaoProximo">
                <a className="page-link" href="#">3</a>
              </li>
              <li className="page-item" id="paginacaoUltimo">
                <a className="page-link" href="#">Último</a>
              </li>
            </ul>
          </nav>
          <div>
            <a className="btn btn-lg btn-success" role="button"><i className="fas fa-plus-square"></i> Incluir novo</a>
          </div>
        </div>
      </div>
      <div>
        <div className="modal fade" id="modalExcluir" tabIndex="-1" data-bs-backdrop="static" data-bs-keyboard="false" aria-labelledby="modalLabel" aria-hidden="true">
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title" id="modalLabel"><i className="fas fa-trash-alt"></i> Confirmar exclusão</h5>
                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
              </div>
              <div className="modal-body">
                <p id="modalExcluirMsg">Confirma exclusão?</p>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-outline-dark" data-bs-dismiss="modal">Não</button>
                <form id="modalExcluirForm" style={{ display: 'inline-block' }} method="post">
                  <button type="submit" className="btn btn-danger">Sim</button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
