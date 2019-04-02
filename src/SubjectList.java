import java.io.Serializable;
import java.util.List;

public class SubjectList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7361593186374300207L;
	
	private List<Subject> subjectList;
	
	public SubjectList() {
		super();
	}

	public SubjectList(List<Subject> subjectList) {
		super();
		this.subjectList = subjectList;
	}

	@Override
	public String toString() {
		return "SubjectList [subjectList=" + subjectList + "]";
	}

	public List<Subject> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<Subject> subjectList) {
		this.subjectList = subjectList;
	}

}
