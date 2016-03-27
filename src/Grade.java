import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import busca.BuscaProfundidade;
import busca.Estado;
import busca.Nodo;


public class Grade implements Estado {

	public static class Professor implements Cloneable {
		
		private final String nome;
		private final Materia[] materiasApto;
		private final int diaDeFolga;
		
		public Professor(String nome, Materia[] materiasApto, int diaDeFolga) {
			this.nome = nome;
			this.materiasApto = materiasApto;
			this.diaDeFolga = diaDeFolga;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof Professor) {
				Professor o = (Professor) obj;
				if (nome.equals(o.nome) && diaDeFolga == o.diaDeFolga) {
					return true;
				}
				
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return nome.hashCode() + diaDeFolga;
		}
		
		@Override
		public Professor clone() {
			return new Professor(nome, materiasApto, diaDeFolga);
		}
		
		@Override
		public String toString() {
			return nome;
		}
	}
	
	public static class Materia implements Cloneable {
		private final String nome;
		private final Professor professor;
		
		public Materia (String nome, Professor professor){
			this.nome = nome;
			this.professor = professor;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof Materia) {
				Materia o = (Materia) obj;
				if (!nome.equals(o.nome)) {
					return false;
				}
				if (professor != o.professor) {
					return false;
				}
				if ((professor != null) && !professor.equals(o.professor)) {
					return false;
				}
				
			}
			return true;
		}
		
		@Override
		public int hashCode() {
			return nome.hashCode() + ((professor != null) ? professor.hashCode() : 0);
		}
		
		@Override
		public Materia clone() {
			return new Materia(nome, professor != null ? professor.clone() : null);
		}
	}
	
	public static class DiaSemana implements Cloneable {
		
		private final Materia primeiraAula;
		private final Materia segundaAula;
		
		public DiaSemana(Materia primeiraAula, Materia segundaAula) {
			this.primeiraAula = primeiraAula;
			this.segundaAula = segundaAula;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof DiaSemana) {
				DiaSemana o = (DiaSemana) obj;
				if (primeiraAula == o.primeiraAula) {
					return true;
				}
				if ((primeiraAula != null && o.primeiraAula != null) && primeiraAula.equals(o.primeiraAula)) {
					return true;
				}
				if (segundaAula == o.segundaAula) {
					return true;
				}
				if ((segundaAula != null && o.segundaAula != null) && segundaAula.equals(o.segundaAula)) {
					return true;
				}
				
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return (primeiraAula != null ? primeiraAula.hashCode() : 0) + (segundaAula != null ? segundaAula.hashCode() : 0);
		}
		
		@Override
		public DiaSemana clone() {
			return new DiaSemana(primeiraAula != null ? primeiraAula.clone() : null, segundaAula != null ? segundaAula.clone() : null);
		}
		
		@Override
		public String toString() {
			return (primeiraAula != null ? primeiraAula.nome + " - " + primeiraAula.professor : "") + " | " + (segundaAula != null ? segundaAula.nome  + " - " + segundaAula.professor: "");
		}
		
	}
	
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
					if (prof.materiasApto[i].equals(new Materia(materia.nome, null))) {
						encaixaMateria(new Materia(materia.nome, prof), lista);	
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
			if (i == materia.professor.diaDeFolga) {
				continue;
			}
			if (grade[i].primeiraAula == null) {
				DiaSemana[] novaGrade = copiaArray(grade);
				novaGrade[i] = new DiaSemana(materia.clone(), novaGrade[i].segundaAula);
				lista.addAll(pegaDiasSucessores(novaGrade, novaGrade[i].primeiraAula, i));
			}
			if (grade[i].segundaAula == null) {
				DiaSemana[] novaGrade = copiaArray(grade);
				novaGrade[i] = new DiaSemana(novaGrade[i].primeiraAula, materia.clone());
				lista.addAll(pegaDiasSucessores(novaGrade, novaGrade[i].segundaAula, i));
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
			if (i == skipDay || i == materia.professor.diaDeFolga) {
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
		List<Materia> materiasDisponiveis = new LinkedList<Grade.Materia>();
		Materia robotica = new Materia("Robótica", null);
		Materia ia = new Materia("Inteligência Artificial", null);
		Materia web = new Materia("Desenvolvimento Web", null);
		Materia cg = new Materia("Computação Gráfica", null);
		Materia ps2 = new Materia("Processos de Software II", null);
		materiasDisponiveis.add(robotica);
		materiasDisponiveis.add(ia);
		materiasDisponiveis.add(web);
		materiasDisponiveis.add(cg);
		materiasDisponiveis.add(ps2);
		
		List<Professor> professores = new LinkedList<Grade.Professor>();
		professores.add(new Professor("Rogério", new Materia[]{ia, cg, ps2}, 1));
		professores.add(new Professor("Jonaya", new Materia[]{ia, robotica}, 0));
		professores.add(new Professor("Marcel", new Materia[]{web}, 0));
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
