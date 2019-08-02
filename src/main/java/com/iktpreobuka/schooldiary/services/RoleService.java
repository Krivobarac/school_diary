package com.iktpreobuka.schooldiary.services;

import com.iktpreobuka.schooldiary.entities.RoleEntity;
import com.iktpreobuka.schooldiary.enums.IRole;

public interface RoleService {
	public void fillRole();
	public RoleEntity getRoleByRole(IRole role);
}
