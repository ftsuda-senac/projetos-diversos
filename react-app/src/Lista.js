import React from 'react';
import { Link } from 'react-router-dom';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

const TableBody = props => {
  const rows = props.content.map(function (row, index) {
    return (
      <tr key={index}>
        <td>{row.id}</td>
        <td>{row.nome}</td>
        <td>{row.dataNascimento}</td>
        <td>
          <Link to={"/edit/" + row.id} className="btn btn-primary"><i className="fa fa-pencil"></i></Link>
          <Button variant="danger" onClick={props.onDeleteClick}><i className="fa fa-trash"></i></Button>
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
        last: false
      },
      paginaAtual: 0,
      ultimaPagina: 0,
      qtd: 10,
      modalDelete: {
        aberto: false,
        itemId: 0
      }
    }
    // Aqui utilizamos o `bind` para que o `this` funcione dentro da nossa callback
    this.handlePageClick = this.handlePageClick.bind(this);
    this.handleModalOpen = this.handleModalOpen.bind(this);
    this.handleModalClose = this.handleModalClose.bind(this);
  }

  loadData(pagina) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/rest/pessoas?pagina=" + pagina + "&qtd=" + this.state.qtd, true);
    const component = this;
    xhr.addEventListener("load", function () {
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

  handlePageClick(event, page) {
    event.preventDefault();
    this.loadData(page);
  }

  handleModalOpen(id) {
    this.setState({ 
      modalDelete: {
        aberto: true,
        itemId: id
      }
    });
  }

  handleModalClose() {
    this.setState({ 
      modalDelete: {
        aberto: false,
        itemId: 0
      }
    });
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
                <TableBody content={this.state.data.content} onDeleteClick={this.handleModalOpen} />
                <tfoot>
                  <tr>
                    <td colSpan="4" style={{ textAlign: 'right' }}>
                      <nav aria-label="Paginação">
                        <ul className="pagination">
                          <li className={"page-item " + (this.state.data.first ? "disabled" : "")}>
                            <a className="page-link" href="#" onClick={(ev) => this.handlePageClick(ev, 0)} >Primeiro</a>
                          </li>
                          {
                            this.state.data.number - 1 >= 0 &&
                            <li className="page-item">
                              <a className="page-link" href="#" onClick={(ev) => this.handlePageClick(ev, this.state.paginaAtual - 1)}>{this.state.data.number}</a>
                            </li>
                          }
                          <li className="page-item active">
                            <a className="page-link active" href="#">{this.state.data.number + 1}<span className="sr-only">(current)</span></a>
                          </li>
                          {
                            this.state.data.number + 1 < this.state.data.totalPages &&
                            <li className="page-item">
                              <a className="page-link" href="#" onClick={(ev) => this.handlePageClick(ev, this.state.paginaAtual + 1)}>{this.state.data.number + 2}</a>
                            </li>
                          }
                          <li className={"page-item " + (this.state.data.last ? "disabled" : "")}>
                            <a className="page-link" href="#" onClick={(ev) => this.handlePageClick(ev, this.state.ultimaPagina)}>Último</a>
                          </li>
                        </ul>
                      </nav>
                    </td>
                  </tr>
                </tfoot>
              </table>

              <Link to="/new" className="btn btn-success"><i className="fa fa-plus-circle"></i> Incluir novo</Link>

              {/* https://react-bootstrap.github.io/components/modal/ */}
              {/*
              <Modal.Dialog>
                <Modal.Header>
                  <Modal.Title>Confirmar remoção</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                  <p>Confirma remoção?</p>
                </Modal.Body>

                <Modal.Footer>
                  <Button variant="secondary" onClick={this.handleModalClose}>Não</Button>
                  <Button variant="danger">Sim</Button>
                </Modal.Footer>
              </Modal.Dialog>
              */ }
            </div>
          </div>
        </div>
      </main>
    );
  }
}

export default Lista;
