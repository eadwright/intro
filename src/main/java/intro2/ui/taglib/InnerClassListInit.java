package intro2.ui.taglib;

import intro2.database.ClassInfo;
import intro2.database.Database;
import intro2.database.reference.InnerClassReference;
import intro2.util.IList;
import lahaina.runtime.State;
import lahaina.runtime.Writer;
import lahaina.tag.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class InnerClassListInit implements Tag {
	public static final String THIS = "this";
	public static final String INNER_CLASSES = "innerClasses";

	private static final Comparator comparator = new InnerRefComparator();

	private static class InnerRefComparator implements Comparator {
		public int compare(Object obj1, Object obj2) {
			InnerClassReference ref1 = (InnerClassReference)obj1;
			InnerClassReference ref2 = (InnerClassReference)obj2;

			int score1 = getScore(ref1);
			int score2 = getScore(ref2);

			if(score1 != score2)
				return score1 - score2;

			if(!ref1.isAnonymous() && !ref2.isAnonymous())
				return ref1.getDeclaredName().compareTo(ref2.getDeclaredName());

			return 0;
		}

		private int getScore(InnerClassReference ref) {
			int score = 0;
			if(ref.isInterface())
				score -= 20000;
			if(ref.isAnonymous())
				score -= 10000;
			if(ref.isPublic())
				score -= 5000;
			if(ref.isProtected())
				score -= 2500;
			if(ref.isPrivate())
				score -= 1225;
			if(ref.isFinal())
				score -= 600;
			if(ref.isAbstract())
				score -= 150;

			return score;
		}
	}

	public InnerClassListInit(State state) {
		ClassInfo ci = (ClassInfo)state.getAttribute(THIS);

		IList refIDs = ci.getRefIDs();
		List refs = new ArrayList();

		Object ref;
		InnerClassReference innerRef;
		for(int i=0;i<refIDs.getSize();i++) {
			ref = Database.getReference(refIDs.get(i));
			if(ref instanceof InnerClassReference) {
				innerRef = (InnerClassReference)ref;
				if(innerRef.getOuterClassID() == ci.getID())
					refs.add(innerRef);
			}
		}

		Collections.sort(refs, comparator);

		state.setAttribute(INNER_CLASSES, refs.toArray());
	}

	public boolean process(Writer out) {
		return false;
	}
}