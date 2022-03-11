package com.viettel.etc.services;

import com.viettel.etc.dto.DoctorDTO;
import com.viettel.etc.dto.FileImportDTO;
import com.viettel.etc.dto.RessetPasswordDTO;
import com.viettel.etc.utils.TeleCareException;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * Autogen class: Lop thong tin cua bac si
 *
 * @author ToolGen
 * @date Wed Aug 19 14:21:51 ICT 2020
 */
public interface DoctorService {


	public Object getDoctors(DoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException;

	public Object getDoctorsExcel(DoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException;

	public Object getDoctorsPublic(DoctorDTO itemParamsEntity) throws TeleCareException;

	public Object updateDoctor(DoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException, IOException;

	public void deleteDoctor(DoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException, IOException;

	public void ressetPassword(RessetPasswordDTO dto) throws TeleCareException;

	public Object createDoctor(DoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException, IOException;

	public Object getDoctorInfo(DoctorDTO itemParamsEntity, Integer doctorId) throws TeleCareException;

	public Object getDoctorByKeycloakUserId(DoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException;

	public Object getTopDoctors(DoctorDTO itemParamsEntity);

	Optional<Path> convertListDataDoctorToExcel(List<DoctorDTO> listDataExport, DoctorDTO dataParams);

	public Object getDoctorHomepageWeb(Authentication authentication, String language) throws TeleCareException, IOException;

	public Object importDoctor(DoctorDTO dto, Authentication authentication) throws TeleCareException, IOException;

	Object createDoctorAndVideoCall(DoctorDTO dataParams, Authentication authentication) throws TeleCareException, IOException;

    Object importDoctorFile(Authentication authentication, FileImportDTO file) throws TeleCareException, IOException, ParseException;

	Object uploadFileImport(Authentication authentication, MultipartFile file, String language) throws IOException, TeleCareException;
}
