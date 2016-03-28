import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.BuscaRecursiva;
import busca.Estado;
import busca.MostraStatusConsole;
import busca.Nodo;


public class Grade implements Estado {

	private final DiaSemana[] grade;
	private final List<Materia> materiasDisponiveis;
	private final List<Professor> professores;
	
	public Grade(DiaSemana[] grade, List<Materia> materiasDisponiveis, List<Professor> professores) {
		this.grade = grade;
		this.materiasDisponiveis = materiasDisponiveis;
		this.professores = professores;
	}
	
	@Override
	public int custo() {
		return 1;
	}

	@Override
	public boolean ehMeta() {
		return materiasDisponiveis.size() == 0;
	}

	@Override
	public List<Estado> sucessores() {
		Set<Estado> lista = new HashSet<Estado>();
		
		List<Estado> result = new LinkedList<Estado>();
		if (materiasDisponiveis.size() > 0) {
			Materia materia = materiasDisponiveis.remove(0);
			for (Professor prof : professores) {
				for (int i = 0; i < prof.materiasApto.length; i++) {
					if (prof.materiasApto[i].equals(materia)) {
						encaixaMateria(new Materia(materia.getNome(), prof, materia.getCargaHoraria()), lista);	
						break;
					}
				}
			}
			result.addAll(lista);
		}
		return result;
	}
	
	private void encaixaMateria(Materia materia, Set<Estado> lista) {
		for (int i = 0; i < grade.length; i++) {
			// Se o dia que está tentando encaixar for o dia de folga do professor, pula esse dia
			if (i == materia.getProfessor().diaDeFolga) {
				continue;
			}
			if (grade[i].primeiraAula == null) {
				DiaSemana[] novaGrade = copiaArray(grade);
				novaGrade[i] = new DiaSemana(materia.clone(), novaGrade[i].segundaAula);
				if (materia.getCargaHoraria() > 2) {
					lista.addAll(pegaDiasSucessores(novaGrade, novaGrade[i].primeiraAula, i));
				} else {
					lista.add(new Grade(novaGrade, copiaArray(materiasDisponiveis), copiaProfArray(professores)));
				}
			}
			if (grade[i].segundaAula == null) {
				DiaSemana[] novaGrade = copiaArray(grade);
				novaGrade[i] = new DiaSemana(novaGrade[i].primeiraAula, materia.clone());
				if (materia.getCargaHoraria() > 2) {
					lista.addAll(pegaDiasSucessores(novaGrade, novaGrade[i].segundaAula, i));
				} else {
					lista.add(new Grade(novaGrade, copiaArray(materiasDisponiveis), copiaProfArray(professores)));
				}
			}
		}
	}
	
	private DiaSemana[] copiaArray(DiaSemana[] array) {
		return Arrays.copyOf(array, array.length);
	}
	
	private List<Materia> copiaArray(List<Materia> list) {
		List<Materia> result = new LinkedList<Materia>();
		result.addAll(list);
		return result;
	}
	
	private List<Professor> copiaProfArray(List<Professor> list) {
		List<Professor> result = new LinkedList<Professor>();
		for (Professor prof : list) {
			result.add(prof.clone());
		}
		return result;
	}
	
	private List<Estado> pegaDiasSucessores(DiaSemana[] grade, Materia materia, int skipDay) {
		List<Estado> result = new LinkedList<Estado>();
		for (int i = 0; i < grade.length; i++) {
			if (i == skipDay || i == materia.getProfessor().diaDeFolga) {
				continue;
			}
			if (grade[i].primeiraAula == null) {
				DiaSemana[] novaGrade = copiaArray(grade);
				novaGrade[i] = new DiaSemana(materia.clone(), novaGrade[i].segundaAula);
				result.add(new Grade(novaGrade, copiaArray(materiasDisponiveis), copiaProfArray(professores)));
			}
			if (grade[i].segundaAula == null) {
				DiaSemana[] novaGrade = copiaArray(grade);
				novaGrade[i] = new DiaSemana(novaGrade[i].primeiraAula, materia.clone());
				result.add(new Grade(novaGrade, copiaArray(materiasDisponiveis), copiaProfArray(professores)));
			}
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Grade) {
			Grade o = (Grade) obj;
			if (grade.length != o.grade.length) {
				return false;
			}
			for (int i = 0; i < grade.length; i++) {
				if (!grade[i].equals(o.grade[i])) {
					return false;
				}
			}
			
			if (!materiasDisponiveis.equals(o.materiasDisponiveis)) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < grade.length; i++) {
			sb.append(grade[i].hashCode());
		}
		sb.append(materiasDisponiveis.hashCode());
		return sb.toString().hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (DiaSemana dia : grade) {
			sb.append(dia.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		DiaSemana[] gradeDias = {
				new DiaSemana(null, null),
				new DiaSemana(null, null),
				new DiaSemana(null, null),
				new DiaSemana(null, null),
				new DiaSemana(null, null)
		};
		List<Materia> materiasDisponiveis = new LinkedList<Materia>();
		Materia robotica = new Materia("Robótica", null, 2);
		Materia ia = new Materia("Inteligência Artificial", null, 4);
		Materia web = new Materia("Desenvolvimento Web", null, 4);
		Materia cg = new Materia("Computação Gráfica", null, 4);
		Materia ps2 = new Materia("Processos de Software II", null, 4);
		Materia optativa = new Materia("Optativa A", null, 2);
		materiasDisponiveis.add(robotica);
		materiasDisponiveis.add(ia);
		materiasDisponiveis.add(web);
		materiasDisponiveis.add(cg);
		materiasDisponiveis.add(ps2);
		materiasDisponiveis.add(optativa);
		
		List<Professor> professores = new LinkedList<Professor>();
		professores.add(new Professor("Rogério", new Materia[]{ia, cg, ps2}, 1));
		professores.add(new Professor("Jonaya", new Materia[]{ia}, 0));
		professores.add(new Professor("Marcel", new Materia[]{web}, 0));
		professores.add(new Professor("Aurelio", new Materia[]{robotica, optativa}, 2));
		Grade problema = new Grade(gradeDias, materiasDisponiveis, professores);
		
		BuscaProfundidade buscaLargura = new BuscaProfundidade(10);
		
		Nodo nodo = buscaLargura.busca(problema);
		
		if (nodo == null) {
			System.out.println("Sem solução");
		} else {
			System.out.println("Solução encontrada: ");
			System.out.println(nodo.montaCaminho());
		}
	}
	
}
