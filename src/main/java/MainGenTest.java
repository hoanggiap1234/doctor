import com.viettel.GenDTOTest;
import com.viettel.etc.kafka.domain.TopicModel;

import java.io.IOException;

public class MainGenTest {
	public static void main(String[] args) throws NoSuchMethodException, IOException, ClassNotFoundException {
//        GenDTOTest.genOneClass(ConsumerModel.class.getSimpleName());
		GenDTOTest.genOneClass(TopicModel.class.getSimpleName());
//        GenControllerTest.genAll(TeleCareException.class,"ErrorApp.ERROR_INPUTPARAMS");
//        GenControllerTest.gen(TeleCareException.class, MedicalHealthcareRegimenDetailController.class.getSimpleName(),"ErrorApp.ERROR_INPUTPARAMS");
	}
}