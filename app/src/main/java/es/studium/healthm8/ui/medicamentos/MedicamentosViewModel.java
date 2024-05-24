package es.studium.healthm8.ui.medicamentos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MedicamentosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MedicamentosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("En construcción");
    }

    public LiveData<String> getText() {
        return mText;
    }
}