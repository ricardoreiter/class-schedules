public class DiaSemana implements Cloneable {
	
	final Materia primeiraAula;
	final Materia segundaAula;
	
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
		return (primeiraAula != null ? primeiraAula.getNome() + " - " + primeiraAula.getProfessor() : "") + " | " + (segundaAula != null ? segundaAula.getNome()  + " - " + segundaAula.getProfessor(): "");
	}
	
}