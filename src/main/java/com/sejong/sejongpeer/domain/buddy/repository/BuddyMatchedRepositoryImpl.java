package com.sejong.sejongpeer.domain.buddy.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.QBuddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.QBuddyMatched;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BuddyMatchedRepositoryImpl extends QuerydslRepositorySupport implements BuddyMatchedRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	public BuddyMatchedRepositoryImpl() {
		super(BuddyMatched.class);
	}

	@Override
	public Optional<BuddyMatched> findByOwnerOrPartner(Buddy owner) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QBuddyMatched qBuddyMatched = QBuddyMatched.buddyMatched;
		QBuddy qBuddyOwner = new QBuddy("owner");
		QBuddy qBuddyPartner = new QBuddy("partner");

		return Optional.ofNullable(queryFactory
			.selectFrom(qBuddyMatched)
			.leftJoin(qBuddyMatched.owner, qBuddyOwner).fetchJoin()
			.leftJoin(qBuddyMatched.partner, qBuddyPartner).fetchJoin()
			.where(qBuddyOwner.eq(owner).or(qBuddyPartner.eq(owner)))
			.fetchOne());
	}

}
