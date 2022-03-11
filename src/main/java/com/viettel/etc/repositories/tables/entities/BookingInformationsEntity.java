package com.viettel.etc.repositories.tables.entities;

import com.viettel.etc.utils.Constants;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Autogen class Entity: Create Entity For Table Name Booking_informations
 *
 * @author ToolGen
 * @date Wed Aug 19 21:30:23 ICT 2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "BOOKING_INFORMATIONS")
public class BookingInformationsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID")
    Integer bookingId;

    @Column(name = "BOOKING_TYPE")
    Integer bookingType;

    @Column(name = "BOOKING_GROUP")
    Integer bookingGroup;

    @Column(name = "TICKET_CODE")
    String ticketCode;

    @Column(name = "HEALTHFACILITIES_CODE")
    String healthfacilitiesCode;

    @Column(name = "DOCTOR_ID")
    Integer doctorId;

    @Column(name = "PATIENT_BOOKING_ID")
    Integer patientBookingId;

    @Column(name = "PATIENT_ID")
    Integer patientId;

    @Column(name = "SERVICE_ID")
    Integer serviceId;

    @Column(name = "SOURCE_ID")
    Integer sourceId;

    @Column(name = "SYMPTOMS_OR_REASON")
    String symptomsOrReason;

    @Column(name = "REGISTER_DATE")
    Date registerDate;

    @Column(name = "REGISTER_TIME_TXT")
    String registerTimeTxt;

    @Column(name = "TIMESLOT_ID")
    Integer timeslotId;

    @Column(name = "CONSULTANT_TIME")
    Integer consultantTime;

    @Column(name = "BOOKING_STATUS")
    Integer bookingStatus;

    @Column(name = "APPOINTMENT_DATE")
    Date appointmentDate;

    @Column(name = "APPOINTMENT_TIME")
    String appointmentTime;

    @Column(name = "APPOINTMENT_DOCTOR_ID")
    Integer appointmentDoctorId;

    @Column(name = "MEDICALEXAMINATION_DATE")
    Date medicalexaminationDate;

    @Column(name = "APPROVE_USER_ID")
    Integer approveUserId;

    @Column(name = "APPROVE_DATE")
    Date approveDate;

    @Column(name = "REASON_REJECT")
    String reasonReject;

    @Column(name = "REJECT_USER_ID")
    Integer rejectUserId;

    @Column(name = "REJECT_DATE")
    Date rejectDate;

    @Column(name = "TOTAL_MONEY")
    Long totalMoney;

    @Column(name = "DISCOUNT_RATES")
    Long discountRates;

    @Column(name = "DISCOUNT_TYPE")
    Integer discountType;

    @Column(name = "DISCOUNT_MONEY")
    Long discountMoney;

    @Column(name = "VOUCHER_MONEY")
    Long voucherMoney;

    @Column(name = "EXCHANGE_POINTS")
    Integer exchangePoints;

    @Column(name = "EXCHANGE_MONEY")
    Long exchangeMoney;

    @Column(name = "POINTS_PLUS")
    Integer pointsPlus;

    @Column(name = "TOTAL_PAYMENT")
    Long totalPayment;

    @Column(name = "PATIENT_HISTORIES_ID")
    Integer patientHistoriesId;

    @Column(name = "PAYMENTS_FORM_ID")
    Integer paymentsFormId;

    @Column(name = "CREATE_USER_ID")
    Integer createUserId;

    @Column(name = "UPDATE_USER_ID")
    Integer updateUserId;

    @Column(name = "IS_DELETE")
    Boolean isDelete = Constants.IS_NOT_DELETE;

    @Column(name = "IS_ACTIVE")
    Boolean isActive = Constants.IS_ACTIVE;

    @Column(name = "UPDATE_DATE")
    @UpdateTimestamp
    Date updateDate;

    @Column(name = "CREATE_DATE")
    @CreationTimestamp
    Date createDate;

    @Column(name = "BOOKING_NUMBER")
    Integer bookingNumber;

    @Column(name = "QR_CODE")
    String qrCode;

    @Column(name = "REGISTER_TIME")
    LocalTime registerTime;

    @Column(name = "APPOINTMENT_TIME_APPROVED")
    Date appointmentTimeApproved;

    @Column(name = "HISTORIES_ID")
    Integer historiesId;
}
