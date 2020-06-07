import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { PessoaService } from '../pessoa.service';
import { InteresseService } from '../interesse.service'
import { Pessoa } from '../pessoa';
import { Interesse } from '../interesse';

@Component({ // <-- Essa expressão chama-se "DECORATOR"
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {

  titulo : string = 'Inclusão de pessoas';
  id: number = null;
  pessoa : Pessoa = {
    nome: '',
    sexo: 0,
    dataNascimento: null,
    interessesId: []
  };
  interesses: Interesse[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private pessoaService : PessoaService,
    private interesseService : InteresseService
  ) { }

  ngOnInit(): void {
    this.getInteresses();
    let id = -1;
    this.route.paramMap.subscribe(params => {
      if (params.get('id')) {
        id = Number(params.get('id'));
      }
    });
    if (id > 0) {
      this.id = id;
      this.titulo = 'Editar pessoa';
      this.getPessoaById(this.id);
    }
  }

  getPessoaById(id: number) {
    this.pessoaService.getById(id)
      .subscribe(pessoa => {
        this.pessoa = pessoa
        for (let interesse of this.interesses) {
          for (let interesseSelecionado of this.pessoa.interessesId) {
            if (interesse.id === interesseSelecionado) {
              interesse.selected = true;
              break;
            }
          }
        }
      }
    );
  }

  getInteresses() {
    this.interesseService.getInteresses()
      .subscribe(interesses => {
        this.interesses = interesses;
        for (let interesse of this.interesses) {
          interesse.selected = false;
        }
      }
    );
  }

  getInteressesString() {
    return JSON.stringify(this.interesses);
  }
  getPessoaString() {
    return JSON.stringify(this.pessoa);
  }

  onSubmit() {
    //alert();
    let interessesId = [];
    for (const interesse of this.interesses) {
      if (interesse.selected) {
        interessesId.push(interesse.id);
      }
    }
    this.pessoa.interessesId = interessesId;
    console.log('**** Dados enviados: ' + JSON.stringify(this.pessoa));
    this.pessoaService.addNew(this.pessoa).subscribe(() => { });
    this.router.navigate(['/']);
  }

}
