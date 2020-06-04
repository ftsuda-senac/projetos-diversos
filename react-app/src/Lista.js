import React from 'react';
import { Link } from 'react-router-dom'

const TableBody = props => {
  const rows = props.data.map(function(row, index) {
    return (
      <tr key={index}>
        <td>{row.id}</td>
        <td>{row.nome}</td>
        <td>{row.dataNascimento}</td>
        <td>
          <Link to={"/edit/" + row.id} className="btn btn-primary"><i className="fa fa-pencil"></i></Link>
          <a href="#" className="btn btn-danger"><i className="fa fa-trash"></i></a>
        </td>
      </tr>
    )
  });
  return <tbody>{rows}</tbody>
}

class Lista extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      data: {
        content: [
          {
            id: 1,
            nome: "João da Silva",
            dataNascimento: "2000-01-01"
          },
          {
            id: 2,
            nome: "Maria de Souza",
            dataNascimento: "2000-02-20"
          },
          {
            id: 3,
            nome: "José dos Santos",
            dataNascimento: "2000-03-25"
          }
        ],
        number: 0,
        totalPages: 99,
        first: true,
        last: false,

      },
      paginaAtual: 0,
      ultimaPagina: 0,
      qtd: 10
    }
  }

  loadData(pagina) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/rest/pessoas?pagina=" + pagina + "&qtd=" + this.state.qtd, true);
    const component = this;
    xhr.addEventListener("load", function() {
      if (xhr.status >= 200 && xhr.status < 400) {
        let data = JSON.parse(xhr.responseText);
        component.setState({
          data: data,
          paginaAtual: data.number,
          ultimaPagina: data.totalPages - 1
        });
      }
    });
    xhr.send();
  }

  componentDidMount() {
    this.loadData(this.state.paginaAtual);
  }

  render() {
    return (
      <main role="main" className="flex-shrink-0">
        {
          this.props.location.state && this.props.location.state.msgSuccess &&
          <div className="alert alert-success" role="alert">
            {this.props.location.state.msgSuccess}
          </div>
        }
      <div className="container">
        <div className="row">
          <div className="col-12">
            <h2>Pessoas - React App</h2>
            <table className="table table-striped">
              <thead className="thead-dark">
                <tr>
                  <th>ID</th>
                  <th>Nome</th>
                  <th>Data nascimento</th>
                  <th>AÇÕES</th>
                </tr>
              </thead>
              <TableBody data={this.state.data.content} />
              <tfoot>
                <tr>
                  <td colSpan="4" style={{textAlign: 'right'}}>
                    <nav aria-label="Paginação">
                      <ul className="pagination">
                        <li className={"page-item " + (this.state.data.first ? "disabled" : "")}>
                          <a className="page-link" href="#" >Primeiro</a>
                        </li>
                        {
                          this.state.data.number - 1 > 0 &&
                          <li className="page-item">
                            <a className="page-link" href="#">{this.state.data.number}</a>
                          </li>
                        }
                        <li className="page-item active">
                          <a className="page-link active" href="#">{this.state.data.number + 1}<span className="sr-only">(current)</span></a>
                        </li>
                        {
                          this.state.data.number + 1 < this.state.data.totalPages &&
                          <li className="page-item">
                            <a className="page-link" href="#">{this.state.data.number + 2}</a>
                          </li>
                        }
                        <li className={"page-item " + (this.state.data.last ? "disabled" : "")}>
                          <a className="page-link" href="#">Último</a>
                        </li>
                      </ul>
                    </nav>
                  </td>
                </tr>
              </tfoot>
            </table>
            <Link to="/new" className="btn btn-success"><i className="fa fa-plus-circle"></i> Incluir novo</Link>
            <div id="modalDelete" className="modal fade" tabIndex="-1" role="dialog">
              <div className="modal-dialog" role="document">
                <div className="modal-content">
                  <header className="modal-header">
                    <h5>Confirmar remoção</h5>
                    <button type="button" className="close" data-dismiss="modal" aria-label="Fechar">
                      <span aria-hidden="true">X</span>
                    </button>
                  </header>
                  <div className="modal-body">
                    <p className="modal-message">Confirma remoção?</p>
                  </div>
                  <footer className="modal-footer">
                    <button type="button" className="btn btn-secondary" data-dismiss="modal">Não</button>
                    <form style={{display: 'inline-block'}} method="post">
                      <button type="submit" className="btn btn-danger">Sim</button>
                    </form>
                  </footer>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
    );
  }
}

export default Lista;
