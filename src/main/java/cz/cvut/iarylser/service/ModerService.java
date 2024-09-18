package cz.cvut.iarylser.service;

import jakarta.persistence.EntityNotFoundException;

public interface ModerService {
    public void blockUser(Long userId) throws EntityNotFoundException;
    public void unblockUser(Long userId)  throws EntityNotFoundException;
}
