
public class Professor implements Cloneable {
		
		final String nome;
		final Materia[] materiasApto;
		final int diaDeFolga;
		
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