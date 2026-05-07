package com.kernotec.driverschedule.person.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.common.util.CommonUtil;
import com.kernotec.driverschedule.person.exception.ContactCategoryException;
import com.kernotec.driverschedule.person.jpa.entity.ContactCategory;
import com.kernotec.driverschedule.person.jpa.enums.ContactCategoryEnum;
import com.kernotec.driverschedule.person.jpa.repository.ContactCategoryRepository;
import com.kernotec.driverschedule.person.rest.dto.response.ContactCategoryLookupResponse;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ContactCategoryService extends BaseServiceImpl<ContactCategory, UUID> {

    private final ContactCategoryRepository repository;

    @Override
    protected String resourceName() {
        return "Contact Category";
    }

    @Override
    protected BaseRepository<ContactCategory, UUID> repository() {
        return repository;
    }

    public Page<ContactCategoryLookupResponse> findAllToLookup(String keyword, Pageable pageable)
    {
        String keywordStr = CommonUtil.getSafeString(keyword);
        return repository.findAllToLookup(keywordStr, pageable);
    }

    public Optional<ContactCategory> findByCode(ContactCategoryEnum code) {
        return repository.findByCode(String.valueOf(code));
    }

    public ContactCategory findByCodeThrow(ContactCategoryEnum code) {
        return findByCode(code).orElseThrow(
            () -> new ContactCategoryException(
                "code.not.found", "'" + code + "'", HttpStatus.NOT_FOUND.value()));
    }

    public UUID findIdByCodeThrow(ContactCategoryEnum code) {
        return findByCodeThrow(code).getId();
    }
}
