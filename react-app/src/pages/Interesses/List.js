import React, { useState } from 'react';

export default function InteressesList() {

  const [msgSucesso, setMsgSucesso] = useState('');
  const [msgErro, setMsgErro] = useState('');
  const [dadosLista, setDadosLista] = useState([]);

  return (
    <div className="row">
      <div className="offset-md-2 col-md-8">
        <h1>Listagem de interesses (VERSÃO React)</h1>
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
              <th scope="col">Ações</th>
            </tr>
          </thead>
          <tbody>
            {
              dadosLista.map(function (dados, idx) {
                return (
                  <tr key={`interesses-${dados.id}`}>
                    <td>{dados.id}</td>
                    <td>{dados.nome}</td>
                    <td>
                      <a className="btn btn-primary" role="button"><i className="fas fa-edit"></i> Alterar</a>
                    </td>
                  </tr>
                );
              })
            }
          </tbody>
        </table>
        <div>
          <a className="btn btn-lg btn-success" role="button"><i className="fas fa-plus-square"></i> Incluir novo</a>
        </div>
      </div>
    </div>
  );
}
