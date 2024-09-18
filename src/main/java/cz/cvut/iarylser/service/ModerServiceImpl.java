package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.entity.UserStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ModerServiceImpl implements ModerService {
    private UserServiceImpl userService;
    @Override
    public void blockUser(Long userId) throws EntityNotFoundException {
        User user = userService.getById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User is not find");
        }
        user.setUserStatus(UserStatus.BLOCKED);
    }
    @Override
    public void unblockUser(Long userId)  throws EntityNotFoundException{
        User user = userService.getById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User is not find");
        }
        user.setUserStatus(UserStatus.ACTIVE);
    }
}
