import React from 'react';
import { Link } from 'react-router-dom';

class Form extends React.Component {

  render() {
    return (
      <main role="main" className="flex-shrink-0">
        <div className="container">
          <div className="row">
            <div className="col-12">
              <h2>Inclusão de pessoa</h2>
              <form id="dadosForm" noValidate>
                <input type="hidden" name="id" value="" />
                <div className="form-group row">
                  <label className="col-2 col-form-label">Nome completo</label>
                  <div className="col-10">
                    <input type="text" className="form-control" required id="nome" name="nome" value="" />
                  </div>
                </div>
                <div className="form-group row">
                  <label className="col-2 col-form-label">Sexo</label>
                  <div className="col-sm-10">
                    <div className="form-check form-check-inline">
                      <input className="form-check-input" type="radio" id="sexoF" value="0" name="sexo" checked="checked" />
                      <label className="form-check-label" htmlFor="sexoF">Feminino</label>
                    </div>
                    <div className="form-check form-check-inline">
                      <input className="form-check-input" type="radio" id="sexoM" value="1" name="sexo" />
                      <label className="form-check-label" htmlFor="sexoM">Masculino</label>
                    </div>
                  </div>
                </div>
                <div className="form-group row">
                  <label className="col-2 col-form-label">Data nascimento</label>
                  <div className="col-10">
                    <input type="date" className="form-control" required id="dataNascimento" name="dataNascimento" value="" />

                  </div>
                </div>
                <div className="form-group row">
                  <label className="col-2 col-form-label">Interesses</label>
                  <div className="col-sm-10">
                    <div className="form-check form-check-inline">
                      <input className="form-check-input" type="checkbox" id="interesse_1" value="1" name="interessesId" />
                      <label className="form-check-label" htmlFor="interesse_1">Tecnologia</label>
                    </div>
                    <div className="form-check form-check-inline">
                      <input className="form-check-input" type="checkbox" id="interesse_2" value="2" name="interessesId" />
                      <label className="form-check-label" htmlFor="interesse_2">Gastronomia</label>
                    </div>
                    <div className="form-check form-check-inline">
                      <input className="form-check-input" type="checkbox" id="interesse_3" value="3" name="interessesId" />
                      <label className="form-check-label" htmlFor="interesse_3">Viagens</label>
                    </div>
                    <div className="form-check form-check-inline">
                      <input className="form-check-input" type="checkbox" id="interesse_4" value="4" name="interessesId" />
                      <label className="form-check-label" htmlFor="interesse_4">Investimentos</label>
                    </div>
                    <div className="form-check form-check-inline">
                      <input className="form-check-input" type="checkbox" id="interesse_5" value="5" name="interessesId" />
                      <label className="form-check-label" htmlFor="interesse_5">Esportes</label>
                    </div>
                  </div>
                </div>
                <div className="text-center">
                  <Link to="/" className="btn btn-light">Voltar</Link>
                  <button type="submit" className="btn btn-primary">Salvar</button>
                </div>
              </form>

            </div>
          </div>
        </div>
      </main>
    );
  }
}

export default Form;
