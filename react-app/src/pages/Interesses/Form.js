import React, { useState } from 'react';

export default function InteressesForm() {

  const [msgSucesso, setMsgSucesso] = useState('');
  const [msgErro, setMsgErro] = useState('');
  const [dados, setDados] = useState(null)

  return (
    <div className="row">
      <div className="offset-md-2 col-md-8">
        <h1>{(dados && dados.id) ? `Alteração de interesse - ID ${dados.id}` : 'Inclusão de interesse'}</h1>
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

        <form method="post" novalidate>

          <div className="row mb-3">
            <label htmlFor="txtNome" className="col-sm-2 col-form-label">Nome</label>
            <div className="col-sm-10">
              <input
                type="text"
                name="nome"
                id="txtNome"
                className="form-control"
                placeholder="Preencha o nome do interesse"
                maxLength="100"
                required
              />
              <div className="invalid-feedback"></div>
            </div>
          </div>

          <div className="row">
            <div className="col-md-3 offset-md-3">
              <div className="d-grid">
                <a role="button" className="btn btn-outline-dark">Cancelar</a>
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