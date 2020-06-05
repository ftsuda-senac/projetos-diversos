import React from 'react';
import { Link } from 'react-router-dom';

class InteressesOptions extends React.Component {

  constructor(props) {
    super(props);
    this.handleInputIntegerValueChange = this.handleInputIntegerValueChange.bind(this);
  }

  handleInputIntegerValueChange(event) {
    const target = event.target;
    console.log("" + target.value + " CHECKED: " + target.checked)
    if (!!target.checked) {
      this.props.onCheckOption(Number(target.value));
    } else {
      this.props.onUncheckOption(Number(target.value));
    }
  }

  render() {
    const selecionados = this.props.valoresSelecionados;
    let component = this;
    const opcoes = this.props.valores.map(function(row, index) {
      const elementId = "interesses_" + row.id;
      let selecionado = false;
      for (const idSelecionado of selecionados) {
        if (row.id === idSelecionado) {
          selecionado = true;
        }
      }
      return (
        <div className="form-check form-check-inline" key={row.id}>
          <input className="form-check-input" type="checkbox" name="interessesId" 
                id={elementId} value={row.id}
                onChange={component.handleInputIntegerValueChange} checked={selecionado} />
          <label className="form-check-label" htmlFor={elementId}>{row.nome}</label>
        </div>
      );
    });
    return <div className="col-sm-10">{opcoes}</div>;
  }
}

class Form extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      nome: "",
      sexo: 0,
      dataNascimento: "",
      interessesId: [],
      interessesValues: [],
      tituloAcao: "Inclusão de pessoa"
    };
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleInputIntegerValueChange = this.handleInputIntegerValueChange.bind(this);
    this.handleCheckOptionChange = this.handleCheckOptionChange.bind(this);
    this.handleUncheckOptionChange = this.handleUncheckOptionChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleInputChange(event) {
    const target = event.target;
    // const value = target.name === 'isGoing' ? target.checked : target.value;
    const value = target.value;
    const name = target.name;
    this.setState({
      [name]: value
    });
  }

  handleInputIntegerValueChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    this.setState({
      [name]: Number(value)
    })
  }

  handleCheckOptionChange(value) {
    let interessesId = [];
    for (const oldValue of this.state.interessesId) {
      interessesId.push(oldValue);
    }
    interessesId.push(value)
    this.setState({
      interessesId: interessesId
    });
  }

  handleUncheckOptionChange(value) {
    let interessesId = [];
    for (const oldValue of this.state.interessesId) {
      if (oldValue !== value) {
        interessesId.push(oldValue);
      }
    }
    this.setState({
      interessesId: interessesId
    });
  }

  handleSubmit(event) {
    // TRATANDO SOMENTE INCLUSAO
    // TODO: ALTERAR FRONT/BACK PARA ACEITAR ATUALIZACAO
    event.preventDefault();
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/rest/pessoas", true);
		xhr.setRequestHeader('content-type', 'application/json');
    xhr.setRequestHeader('accept', 'application/json');
    let component = this;
		let data = {
      nome: this.state.nome,
      sexo: this.state.sexo,
      dataNascimento: this.state.dataNascimento,
      interessesId: this.state.interessesId
    }
		xhr.addEventListener('load', function() {
      let msg = {}
			if (this.status >= 200 && this.status < 400) {
        msg.msgSuccess = "Pessoa salva com sucesso"
      }
      component.props.history.push("/", msg );
    });
    xhr.send(JSON.stringify(data));
  }

  loadInteresses() {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/rest/interesses", true);
    const component = this;
    xhr.addEventListener("load", function() {
      if (xhr.status >= 200 && xhr.status < 400) {
        let data = JSON.parse(xhr.responseText);
        component.setState({
          interessesValues: data
        });
      }
    });
    xhr.send();
  }

  loadData(id) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/rest/pessoas/" + id, true);
    const component = this;
    xhr.addEventListener("load", function() {
      if (xhr.status >= 200 && xhr.status < 400) {
        let data = JSON.parse(xhr.responseText);
        let interessesId = [];
        for (const interesse of data.interesses) {
          interessesId.push(interesse.id)
        }
        component.setState({
          id: data.id,
          nome: data.nome,
          sexo: data.sexo,
          dataNascimento: data.dataNascimento,
          interessesId: interessesId
        });
      }
    });
    xhr.send();
  }

  componentDidMount() {
    this.loadInteresses();

    // Se informar ID no parametro, carregar dados a serem editados
    if (this.props.match.params.id) {
      this.loadData(this.props.match.params.id);
      this.setState({
        tituloAcao: "Editar pessoa"
      });
    }
  }

  render() {
    return (
      <main role="main" className="flex-shrink-0">
        <div className="container">
          <div className="row">
            <div className="col-12">
              <h2>{this.state.tituloAcao}</h2>
              <form id="dadosForm" noValidate onSubmit={this.handleSubmit}>
                <div className="form-group row">
                  <label className="col-2 col-form-label">Nome completo</label>
                  <div className="col-10">
                    <input type="text" className="form-control" required id="nome" name="nome" value={this.state.nome} onChange={this.handleInputChange} />
                  </div>
                </div>
                <div className="form-group row">
                  <label className="col-2 col-form-label">Sexo</label>
                  <div className="col-sm-10">
                    <div className="form-check form-check-inline">
                      <input className="form-check-input" type="radio" id="sexoF" name="sexo" value="0" onChange={this.handleInputIntegerValueChange} checked={this.state.sexo === 0} />
                      <label className="form-check-label" htmlFor="sexoF">Feminino</label>
                    </div>
                    <div className="form-check form-check-inline">
                      <input className="form-check-input" type="radio" id="sexoM" name="sexo" value="1" onChange={this.handleInputIntegerValueChange} checked={this.state.sexo === 1} />
                      <label className="form-check-label" htmlFor="sexoM">Masculino</label>
                    </div>
                  </div>
                </div>
                <div className="form-group row">
                  <label className="col-2 col-form-label">Data nascimento</label>
                  <div className="col-10">
                    <input type="date" className="form-control" required id="dataNascimento" name="dataNascimento" value={this.state.dataNascimento} onChange={this.handleInputChange} />
                  </div>
                </div>
                <div className="form-group row">
                  <label className="col-2 col-form-label">Interesses</label>
                  <InteressesOptions valores={this.state.interessesValues}
                                valoresSelecionados={this.state.interessesId}
                                onCheckOption={this.handleCheckOptionChange}
                                onUncheckOption={this.handleUncheckOptionChange} />
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
