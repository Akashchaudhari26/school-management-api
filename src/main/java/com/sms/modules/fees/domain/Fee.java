package com.sms.modules.fees.domain;

import lombok.*;
import jakarta.persistence.Id;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fees", uniqueConstraints = @jakarta.persistence.UniqueConstraint(name = "uk_fee_student_year", columnNames = {
		"student_id", "academic_year" }))
public class Fee {

	@Id

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
	private String id;

	private String studentId;
	private String academicYear;

	@ElementCollection
	@CollectionTable(name = "fee_items", joinColumns = @jakarta.persistence.JoinColumn(name = "fee_id"))
	@OrderColumn(name = "item_order")
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @jakarta.persistence.Column(name = "name")),
			@AttributeOverride(name = "amount", column = @jakarta.persistence.Column(name = "amount", precision = 12, scale = 2))
	})
	private List<FeeItem> feeItems;
	@Column(precision = 12, scale = 2)
	private BigDecimal totalAmount;
	@Column(precision = 12, scale = 2)
	private BigDecimal paidAmount;
	@Column(precision = 12, scale = 2)
	private BigDecimal dueAmount;

	@Enumerated(jakarta.persistence.EnumType.STRING)
	private FeeStatus status;
	@ElementCollection
	@CollectionTable(name = "fee_payments", joinColumns = @jakarta.persistence.JoinColumn(name = "fee_id"))
	@jakarta.persistence.OrderColumn(name = "payment_order")
	@AttributeOverrides({
			@AttributeOverride(name = "receiptNo", column = @jakarta.persistence.Column(name = "receipt_no", unique = true)),
			@AttributeOverride(name = "amountPaid", column = @jakarta.persistence.Column(name = "amount_paid", precision = 12, scale = 2)),
			@AttributeOverride(name = "mode", column = @jakarta.persistence.Column(name = "mode")),
			@AttributeOverride(name = "collectedBy", column = @jakarta.persistence.Column(name = "collected_by")),
			@AttributeOverride(name = "paidAt", column = @jakarta.persistence.Column(name = "paid_at"))
	})
	private List<FeePayment> payments;
}
