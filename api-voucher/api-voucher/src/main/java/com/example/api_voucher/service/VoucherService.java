package com.example.api_voucher.service;

import com.example.api_voucher.model.Voucher;
import com.example.api_voucher.model.VoucherUsage;
import com.example.api_voucher.repository.VoucherRepository;
import com.example.api_voucher.repository.VoucherUsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherUsageRepository voucherUsageRepository;

    public VoucherService(VoucherRepository voucherRepository,
                          VoucherUsageRepository voucherUsageRepository) {
        this.voucherRepository = voucherRepository;
        this.voucherUsageRepository = voucherUsageRepository;
    }

    public Voucher createVoucher(Voucher voucher) {
        voucher.setCreatedAt(LocalDateTime.now());
        if (voucher.getCurrentUses() == null) {
            voucher.setCurrentUses(0);
        }
        if (voucher.getActive() == null) {
            voucher.setActive(true);
        }
        return voucherRepository.save(voucher);
    }

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> getVoucherByCode(String code) {
        return voucherRepository.findByCode(code);
    }

    @Transactional
    public VoucherUsage applyVoucher(String code, String usedBy) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Voucher not found with code: " + code));

        if (!voucher.getActive()) {
            throw new RuntimeException("Voucher is not active");
        }

        if (voucher.getExpirationDate() != null && voucher.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Voucher has expired");
        }

        if (voucher.getMaxUses() != null && voucher.getCurrentUses() >= voucher.getMaxUses()) {
            throw new RuntimeException("Voucher has reached maximum number of uses");
        }

        voucher.setCurrentUses(voucher.getCurrentUses() + 1);
        voucherRepository.save(voucher);

        VoucherUsage usage = new VoucherUsage();
        usage.setVoucher(voucher);
        usage.setUsedBy(usedBy);
        usage.setUsedAt(LocalDateTime.now());

        return voucherUsageRepository.save(usage);
    }

    public Voucher deactivateVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found with id: " + id));
        voucher.setActive(false);
        return voucherRepository.save(voucher);
    }

    public void deleteVoucher(Long id) {
        if (!voucherRepository.existsById(id)) {
            throw new RuntimeException("Voucher not found with id: " + id);
        }
        voucherRepository.deleteById(id);
    }

    public List<VoucherUsage> getUsageByVoucherId(Long voucherId) {
        return voucherUsageRepository.findByVoucherId(voucherId);
    }
}
