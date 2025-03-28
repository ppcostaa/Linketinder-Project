#include <stdio.h>

int main()
{
  float alturaDegraus;
  printf("Digite a altura dos degraus: ");
  scanf("%f", &alturaDegraus);
  float alturaUsuario;
  printf("Digite a altura que voce deseja alcancar: ");
  scanf("%f", &alturaUsuario);

  float numDegraus = alturaUsuario/alturaDegraus;
  printf("Voce precisa subir %f degraus para alcancar a altura desejada.\n", numDegraus);


  return 0;
}