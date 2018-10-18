/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.sorteio;

/**
 *
 * @author Fernando
 */
public class Apresentacao {

  private String apresentador;

  private String avaliador;

  public Apresentacao() {
  }

  public Apresentacao(String apresentador, String avaliador) {
    this.apresentador = apresentador;
    this.avaliador = avaliador;
  }

  public String getApresentador() {
    return apresentador;
  }

  public void setApresentador(String apresentador) {
    this.apresentador = apresentador;
  }

  public String getAvaliador() {
    return avaliador;
  }

  public void setAvaliador(String avaliador) {
    this.avaliador = avaliador;
  }

}
