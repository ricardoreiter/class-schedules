public class Materia implements Cloneable {
	private final String nome;
	private final Professor professor;
	private final int cargaHoraria;
	
	public Materia (String nome, Professor professor, int cargaHoraria){
		this.nome = nome;
		this.professor = professor;
		this.cargaHoraria = cargaHoraria;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Materia) {
			Materia o = (Materia) obj;
			if (!getNome().equals(o.getNome())) {
				return false;
			}
			if (getProfessor() != o.getProfessor()) {
				return false;
			}
			if ((getProfessor() != null) && !getProfessor().equals(o.getProfessor())) {
				return false;
			}
			
			if (getCargaHoraria() != o.getCargaHoraria()) {
				return false;
			}
			
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return getNome().hashCode() + ((getProfessor() != null) ? getProfessor().hashCode() : 0);
	}
	
	@Override
	public Materia clone() {
		return new Materia(getNome(), getProfessor() != null ? getProfessor().clone() : null, getCargaHoraria());
	}

	public Professor getProfessor() {
		return professor;
	}

	public String getNome() {
		return nome;
	}

	public int getCargaHoraria() {
		return cargaHoraria;
	}
}